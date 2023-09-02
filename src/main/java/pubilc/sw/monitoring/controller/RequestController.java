/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pubilc.sw.monitoring.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pubilc.sw.monitoring.SessionManager;
import pubilc.sw.monitoring.dto.RequestDTO;
import pubilc.sw.monitoring.service.RequestService;

/**
 *
 * @author parkchaebin
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/project/request")
public class RequestController {

    private final RequestService requestService;
    private final SessionManager sessionManager;
    
    @GetMapping("/request")
    public String request(Model model) {
        
        List<RequestDTO> requestDTOs = requestService.getRequests(sessionManager.getProjectId());
        model.addAttribute("requestDTOs", requestDTOs);  // 요구사항 목록 

        List<String> excelNames = requestService.getExcelNames(sessionManager.getProjectId());  // 엑셀 파일 리스트 
        model.addAttribute("excelNames", excelNames); 
        
        return "project/request/request";
    }
    
    
    // 해당 프로젝트의 모든 요구사항 
    @GetMapping("/getRequests")   
    public @ResponseBody List<RequestDTO> getRequests() {
        return requestService.getRequests(sessionManager.getProjectId());
    }
    
    
    // 요구사항 저장 및 수정 
    @PostMapping("/save")
    public @ResponseBody String saveRequests(@RequestBody List<RequestDTO> requestDTOList, RedirectAttributes attrs) {
        
        for (RequestDTO requestDTO : requestDTOList) {
            requestDTO.setPid(sessionManager.getProjectId());
        }
        
        if(requestService.saveRequests(requestDTOList)){
            attrs.addFlashAttribute("msg", "요구사항 저장 성공하였습니다.");
        } else{
            attrs.addFlashAttribute("msg", "요구사항 저장 실패하였습니다.");
        }

        return "redirect:/project/request/request";
    }
    
    
    // 프로젝트 멤버 중 입력한 값이 들어간 이름 검색
    @GetMapping("/searchUserNames")
    public @ResponseBody List<String> searchUserNames(@RequestParam String username) {
        return requestService.searchUserNames(username,sessionManager.getProjectId());
    }
    
    
    // 요구사항 삭제 
    @PostMapping("/delete")
    public @ResponseBody String deleteRequest(@RequestParam("frid") Long frid, RedirectAttributes attrs) {

        if (requestService.deleteRequestByFrid(frid)) {
            attrs.addFlashAttribute("msg", "요구사항 삭제 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "요구사항 삭제 실패하였습니다.");
        }
        
        return "redirect:/project/request/request";
    }
    
    
    // 요구사항 여러개 삭제 
    @PostMapping("/deletes")
    public @ResponseBody String deleteRequests(@RequestParam(value = "frids", required = false) List<Long> frids, RedirectAttributes attrs) {
        if (frids != null && !frids.isEmpty()) {
            
            if (requestService.deleteRequestByFrids(frids)) {
                attrs.addFlashAttribute("msg", "선택된 요구사항 삭제 성공했습니다.");
            } else {
                attrs.addFlashAttribute("msg", "선택된 요구사항 삭제 실패했습니다.");
            }
        } else {
            attrs.addFlashAttribute("msg", "선택된 요구사항이 없습니다.");
        }

        return "redirect:/project/request/request";
    }
    
    
    // 해당 프로젝트의 모든 요구사항 삭제 
    @PostMapping("/deleteAll")
    public @ResponseBody String deleteRequestsByPid(RedirectAttributes attrs) {

        if (requestService.deleteRequestsByPid(sessionManager.getProjectId())) {
            attrs.addFlashAttribute("msg", "요구사항 전체 삭제 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "요구사항 전체 삭제 실패하였습니다.");
        }
        
        return "redirect:/project/request/request";
    }


    // 요구사항 엑셀 파일 생성 (엑셀 생성 후 폴더에 저장함) 
    @GetMapping("/createExcel")
    public @ResponseBody void createExcel(HttpServletResponse response, RedirectAttributes attrs) throws IOException {
        List<RequestDTO> requestDTOs = requestService.getRequests(sessionManager.getProjectId());  // 요구사항 목록 

        if (requestService.createRequestExcel(requestDTOs)) {
            attrs.addFlashAttribute("msg", "요구사항 엑셀 파일 생성 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "요구사항 엑셀 파일 생성 실패하였습니다.");
        }

    }

    
    // 요구사항 엑셀 다운 
    @GetMapping("/download")
    public ResponseEntity<Resource> download(HttpServletRequest request){
        return requestService.downloadFile(request.getParameter("filename"), String.valueOf(sessionManager.getProjectId()));
    }

    
    // 요구사항 엑셀 파일 생성 후 다운 (엑셀 생성 후 폴더에 저장하지 않음) 
    @GetMapping("/createDownRequestExcel")
    public void createDownRequestExcel(HttpServletResponse response) throws IOException {
        List<RequestDTO> requestDTOs = requestService.getRequests(sessionManager.getProjectId());
        requestService.createDownRequestExcel(requestDTOs, response);
    }


}
