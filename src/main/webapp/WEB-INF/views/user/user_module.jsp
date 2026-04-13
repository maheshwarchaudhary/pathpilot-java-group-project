<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if(session == null || session.getAttribute("role") == null){
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String phase = request.getParameter("phase");
    String title = request.getParameter("title");
    if(phase == null) phase = "Phase 1";
    if(title == null) title = "Career Roadmap";

    // Content logic
    String sectionTitle = "";
    String sectionDesc = "";
    String pdfName = "";
    String videoUrl = "";

    // Navigation logic
    String leftBtnLabel = "Previous";
    String leftBtnUrl = "";

    if ("Phase 1".equals(phase) || "Phase1".equals(phase)) {
        sectionTitle = "1.1 Setting up the Environment";
        sectionDesc = "Install your IDE and set up the local development server.";
        pdfName = "Environment_Setup.pdf";
        videoUrl = "https://www.youtube.com/embed/zJSY8tJY_7s";
        leftBtnLabel = "Back to Roadmap";
        leftBtnUrl = request.getContextPath() + "/user/progress?title=" + title;
    } else if ("Phase 2".equals(phase) || "Phase2".equals(phase)) {
        sectionTitle = "2.1 JavaScript Fundamentals";
        sectionDesc = "Master ES6, Variables, and DOM manipulation.";
        pdfName = "JS_Basics.pdf";
        videoUrl = "https://www.youtube.com/embed/PkZNo7MFNFg";
        leftBtnUrl = request.getContextPath() + "/user/module?phase=Phase 1&title=" + title;
    } else if ("Phase 3".equals(phase) || "Phase3".equals(phase)) {
        sectionTitle = "3.1 React & Frameworks";
        sectionDesc = "Introduction to Component-based architecture.";
        pdfName = "React_Guide.pdf";
        videoUrl = "https://www.youtube.com/embed/w7ejDZ8SWv8";
        leftBtnUrl = request.getContextPath() + "/user/module?phase=Phase 2&title=" + title;
    } else if ("Phase 4".equals(phase) || "Phase4".equals(phase)) {
        sectionTitle = "4.1 Final Capstone Project";
        sectionDesc = "Build and deploy your first full-stack application.";
        pdfName = "Final_Project_Specs.pdf";
        videoUrl = "https://www.youtube.com/embed/p1QU3kLFPdg";
        leftBtnUrl = request.getContextPath() + "/user/module?phase=Phase 3&title=" + title;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Module - <%=phase%></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; }
        .interaction-card { transition: border-color 0.3s ease; cursor: pointer; }
        .interaction-card.active { border-color: #4913ec; background-color: #f5f3ff; }
    </style>
</head>

<body class="bg-gray-50 pb-32">

<div class="max-w-5xl mx-auto py-12 px-6">

    <div class="mb-8">
        <nav class="flex text-sm text-gray-500 mb-2 gap-2">
            <a href="<%=request.getContextPath()%>/user/progress?title=<%=title%>" class="hover:text-indigo-600 font-medium">Roadmap</a>
            <span>/</span>
            <span class="text-gray-900 font-medium"><%=phase%></span>
        </nav>
        <h1 class="text-3xl font-bold text-gray-900 tracking-tight"><%=phase%>: <%=title%></h1>
    </div>

    <div class="bg-white rounded-[2rem] shadow-sm border border-gray-100 overflow-hidden mb-8">
        
        <div class="p-8 border-b border-gray-50">
            <h2 class="text-xl font-bold text-gray-800 mb-4"><%=sectionTitle%></h2>
            <p class="text-gray-600 font-medium"><%=sectionDesc%></p>
        </div>

        <div id="pdfSection" onclick="markInteraction('pdf')" class="interaction-card bg-gray-100 p-8 flex justify-center border-2 border-transparent border-dashed m-4 rounded-xl hover:bg-gray-200/50 transition-all">
            <div class="w-full max-w-2xl aspect-[3/4] bg-white shadow-lg rounded-lg flex flex-col items-center justify-center border group">
                <i class="fas fa-file-pdf text-red-500 text-6xl mb-4 group-hover:scale-110 transition-transform"></i>
                <h4 class="font-bold text-gray-800 tracking-tight"><%=pdfName%></h4>
                <p id="pdfStatus" class="text-xs text-gray-400 mt-2 font-semibold uppercase tracking-wider">Click to Open Material</p>
            </div>
        </div>

        <div id="videoSection" onclick="markInteraction('video')" class="interaction-card p-8 bg-gray-50 border-2 border-transparent border-dashed m-4 rounded-xl hover:bg-gray-200/50 transition-all">
            <div class="rounded-xl overflow-hidden shadow-2xl bg-black aspect-video relative">
                <iframe id="courseVideo" class="w-full h-full" src="<%=videoUrl%>" frameborder="0" allowfullscreen></iframe>
            </div>
            <p id="videoStatus" class="text-center text-xs text-gray-400 mt-4 font-semibold uppercase tracking-wider">Interact with Video to Proceed</p>
        </div>

        <div id="quizUnlockSection" class="hidden p-10 border-t border-gray-100 bg-indigo-50/20 text-center">
            <div class="max-w-md mx-auto">
                <div class="w-16 h-16 bg-white rounded-2xl shadow-sm flex items-center justify-center mx-auto mb-4 border border-indigo-100">
                    <i class="fas fa-bolt text-indigo-600 text-2xl"></i>
                </div>
                <h3 class="text-xl font-bold text-gray-800 mb-2">Ready for Assessment?</h3>
                <p class="text-sm text-gray-500 mb-8">You've completed the learning materials. Take the quick quiz to unlock the next milestone.</p>
                
                <a href="<%=request.getContextPath()%>/user/quiz?phase=<%=phase%>&title=<%=title%>" 
                   class="inline-flex items-center gap-3 px-10 py-4 rounded-2xl font-bold text-white bg-indigo-600 hover:bg-indigo-700 shadow-xl shadow-indigo-100 transition active:scale-95">
                    Start Assessment <i class="fas fa-arrow-right text-xs"></i>
                </a>
            </div>
        </div>
    </div>

    <div class="flex justify-between items-center border-t border-gray-200 pt-8">
        <a href="<%=leftBtnUrl%>" class="flex items-center gap-2 px-6 py-3 rounded-xl font-semibold text-gray-600 bg-white border hover:bg-gray-50 shadow-sm transition active:scale-95">
            <i class="fas fa-arrow-left text-xs"></i> <%=leftBtnLabel%>
        </a>
        
        <p id="lockMessage" class="text-xs font-bold text-gray-400 uppercase tracking-widest italic">
            <i class="fas fa-lock mr-2"></i> Finish materials to proceed
        </p>
    </div>
</div>

<script>
    let pdfOpened = false;
    let videoInteracted = false;

    function markInteraction(type) {
        if (type === 'pdf') {
            pdfOpened = true;
            document.getElementById('pdfSection').classList.add('active');
            document.getElementById('pdfStatus').innerText = "✓ Material Reviewed";
            document.getElementById('pdfStatus').classList.replace('text-gray-400', 'text-indigo-600');
        }
        if (type === 'video') {
            videoInteracted = true;
            document.getElementById('videoSection').classList.add('active');
            document.getElementById('videoStatus').innerText = "✓ Video Viewed";
            document.getElementById('videoStatus').classList.replace('text-gray-400', 'text-indigo-600');
        }

        // If both are done, show the Quiz CTA and hide the lock message
        if(pdfOpened && videoInteracted) {
            document.getElementById('quizUnlockSection').classList.remove('hidden');
            document.getElementById('lockMessage').classList.add('hidden');
            
            Swal.fire({
                title: 'Phase Materials Complete!',
                text: 'Assessment is now unlocked. Are you ready to test your knowledge?',
                icon: 'success',
                confirmButtonColor: '#4913ec',
                showCancelButton: true,
                confirmButtonText: 'Let\'s Go!'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "<%=request.getContextPath()%>/user/quiz?phase=<%=phase%>&title=<%=title%>";
                }
            });
        }
    }
</script>

</body>
</html>