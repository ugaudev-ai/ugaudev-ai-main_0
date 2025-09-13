package com.user.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.user.appresource.AppConst;
import com.user.entity.Role;
import com.user.entity.User;
import com.user.repository.RoleRepository;
import com.user.repository.UserRepository;
import com.user.request.RoleReq;
import com.user.request.UserReq;
import com.user.response.EndResult;
import com.user.service.RoleService;
import com.user.service.UserService;

@RestController
@RequestMapping("/api/master")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired private UserRepository userRepo;
	@Autowired private UserService userServ;
	@Autowired private RoleRepository roleRepo;
	@Autowired private RoleService roleServ;

	@PostMapping("/users")
	public String createUser(@RequestBody UserReq req) {
		log.info("Received request: " + req);
		String finalResp = null;
		EndResult result = null;
		try {
			User createdUser = userServ.createUser(req);
			result= setEndResult(result,AppConst.SUCCESS, "User data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdUser);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateUser(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid User",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateUser(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	//	@GetMapping("/users")
	//	public String getAllUsers(
	//		//@RequestParam(defaultValue = "0") int page,
	//		   // @RequestParam(defaultValue = "10") int size
	//		    ) {
	//		String finalResp = null;
	//		List<User> UserList = null;
	//		EndResult result = null;
	//		try {
	//			//Sort sort = Sort.by(Sort.Direction.ASC, "id");
	//	        //Pageable pageable = PageRequest.of(page, size, sort);
	//			//Sort sort = Sort.by(Sort.Direction.ASC, "id");
	//			//Pageable pageable = PageRequest.of(0, 2, sort);
	//			UserList = (List<User>) userRepo.findAll();
	//			result= setEndResult(result,AppConst.SUCCESS, "User data Recieved",AppConst.ERRORCODE_SUCCESS);
	//			result.setData(UserList);
	//			ObjectMapper mapper = new ObjectMapper();
	//			finalResp = mapper.writeValueAsString(result);
	//			log.info("In getAllUsers(), finalResp:"+finalResp);
	//		} catch (Exception e) {
	//			result= setEndResult(result,AppConst.FAILURE,"Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
	//			finalResp = new Gson().toJson(result);
	//			log.error("In getAllUsers(), finalResp:"+finalResp);
	//			return finalResp;
	//		}
	//		return finalResp;
	//	}

	@GetMapping("/users")
	public String getAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<User> adminPage = userRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Admins data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", adminPage.getContent());
			data.put("total", adminPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllAdmins(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllAdmins(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}


	@PostMapping("/user/get")
	public String getUser(@RequestBody UserReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			User enq = userServ.getUser(req);
			result= setEndResult(result,AppConst.SUCCESS, "User data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(enq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getUser(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getUser(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/user/update")
	public String updateUser(@RequestBody UserReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			User updatedUser = userServ.updateUser(req);
			result= setEndResult(result,AppConst.SUCCESS, "User data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedUser);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateUser(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateUser(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/user/delete")
	public String deleteUser(@RequestBody UserReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			User deletedUser = userServ.deleteUser(req);
			result= setEndResult(result,AppConst.SUCCESS, "User data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedUser);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteUser(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteUser(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/roles")
	public String createRole(@RequestBody RoleReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Role createdRole = roleServ.createRole(req);
			result= setEndResult(result,AppConst.SUCCESS, "Role data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdRole);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateRole(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Role",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateRole(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/role/get")
	public String getRole(@RequestBody RoleReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Role getRole = roleServ.getRole(req);
			result= setEndResult(result,AppConst.SUCCESS, "Recived Role data",AppConst.ERRORCODE_SUCCESS);
			result.setData(getRole);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getRole(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getRole(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping("/roles")
//	public String getAllRoles() {
//		String finalResp = null;
//		List<Role> roleList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			roleList = (List<Role>) roleRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Roles data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(roleList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllRoles(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE,"Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllRoles(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping("/roles")
	public String getAllRoles(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Role> rolePage = roleRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Roles data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", rolePage.getContent());
			data.put("total", rolePage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllRoles(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllRoles(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/role/update")
	public String updateRole(@RequestBody RoleReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Role updatedRole = roleServ.updateRole(req);
			result= setEndResult(result,AppConst.SUCCESS, "Role data Updated!",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedRole);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateRole(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateRole(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/role/delete")
	public String deleteRole(@RequestBody RoleReq req) throws JsonProcessingException {
		String finalResp = null;
		EndResult result = null;
		try {
			Role deletedRole = roleServ.deleteRole(req);
			result= setEndResult(result,AppConst.SUCCESS, "Role data deleted!",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedRole);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteRole(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			//			ObjectMapper mapper = new ObjectMapper();
			//			finalResp = mapper.writeValueAsString(result);
			log.error("In deleteRole(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	public EndResult setEndResult(EndResult result,String status, String message, String errorCode) {
		if(result==null) {
			result = new EndResult();
		}
		result.setMessage(message);
		result.setStatus(status);
		result.setErrorCode(errorCode);
		return result;
	}

}
