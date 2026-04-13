<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 🔐 SERVER-SIDE SESSION + CACHE VALIDATION
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if(session == null || session.getAttribute("role") == null){
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learning Progress - PathPilot</title>
    
    <!-- External Resources -->
    <script src="https://cdn.tailwindcss.com?plugins=forms"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Poppins:wght@500;600;700;800&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet"/>

    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: { 
                        primary: "#4913ec",
                        "primary-dark": "#3a0fb5",
                    },
                    fontFamily: { 
                        sans: ["'Plus Jakarta Sans'", "sans-serif"],
                        heading: ["Poppins", "sans-serif"]
                    }
                }
            }
        }
    </script>
    
    <style type="text/tailwindcss">
        body { font-family: 'Plus Jakarta Sans', sans-serif; @apply bg-[#f8f9fc] antialiased; }
        h1, h2, h3, .font-800 { font-family: 'Poppins', sans-serif; font-weight: 800; }
    </style>
</head>

<body>

<%-- Student Navbar --%>
<jsp:include page="/WEB-INF/views/components/student_navbar.jsp"/>

<div class="max-w-7xl mx-auto py-16 px-6">

    <%-- Header Section --%>
    <div class="flex flex-col md:flex-row justify-between items-center mb-12 gap-6">
        <div>
            <span class="text-[10px] text-primary uppercase font-black tracking-widest bg-indigo-50 px-3 py-1 rounded-md">Enrolled Career Path</span>
            <h1 class="text-4xl text-gray-900 mt-2 tracking-tight">
                ${not empty param.title ? param.title : 'My Roadmap'}
            </h1>
            <p class="text-gray-500 mt-2 font-medium">Enrollment Successful 🎉 — Start your journey now.</p>
        </div>
        
        <a href="${pageContext.request.contextPath}/student/career"
           class="bg-white border border-gray-200 text-gray-700 px-8 py-3 rounded-2xl font-bold shadow-sm hover:bg-gray-50 transition active:scale-95">
           Back to Dashboard
        </a>
    </div>

    <%-- Stats Grid --%>
    <div class="grid md:grid-cols-3 gap-8 mb-12">
        <div class="bg-white p-8 rounded-[2rem] shadow-sm border border-gray-100 text-center">
            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Overall Progress</p>
            <h2 class="text-5xl text-primary mt-3 tracking-tighter">25%</h2>
            <p class="text-gray-400 font-bold text-xs mt-2">COMPLETED</p>
        </div>

        <div class="bg-white p-8 rounded-[2rem] shadow-sm border border-gray-100 text-center">
            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Learning Time</p>
            <h2 class="text-4xl text-gray-800 mt-3 tracking-tight">12 Hours</h2>
            <p class="text-gray-400 text-xs font-bold mt-2">THIS WEEK</p>
        </div>

        <div class="bg-white p-8 rounded-[2rem] shadow-sm border border-gray-100 text-center">
            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Current Streak</p>
            <h2 class="text-4xl text-gray-800 mt-3 tracking-tight">3 Days 🔥</h2>
            <p class="text-gray-400 text-xs font-bold mt-2">KEEP GOING</p>
        </div>
    </div>

    <%-- Phase List --%>
    <div class="space-y-6">

        <%-- Phase 1 --%>
        <div class="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm">
            <span class="text-[10px] text-green-600 font-black uppercase tracking-widest">Phase 1 • Completed</span>
            <h3 class="text-xl text-gray-900 mt-1">Web Development Basics</h3>
            <p class="text-sm text-gray-500 mt-1 font-medium">HTML, CSS, Git fundamentals.</p>

            <div class="flex items-center justify-between mt-8">
                <div class="flex-1 mr-10">
                    <div class="w-full bg-gray-100 h-2 rounded-full overflow-hidden">
                        <div class="bg-green-500 h-2 rounded-full w-full shadow-sm"></div>
                    </div>
                    <p class="text-[10px] text-green-600 mt-3 font-black uppercase tracking-widest">100% Complete</p>
                </div>
                <a href="${pageContext.request.contextPath}/student/module?phase=Phase1&title=${param.title}" 
                   class="px-8 py-3 text-xs font-black text-green-600 border-2 border-green-600 rounded-xl hover:bg-green-50 transition uppercase tracking-widest">
                    Review
                </a>
            </div>
        </div>

        <%-- Phase 2 --%>
        <div class="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm ring-2 ring-primary/5">
            <span class="text-[10px] text-primary font-black uppercase tracking-widest">Phase 2 • In Progress</span>
            <h3 class="text-xl text-gray-900 mt-1">JavaScript Mastery</h3>
            <p class="text-sm text-gray-500 mt-1 font-medium">Async JS, ES6, DOM, Debugging.</p>

            <div class="flex items-center justify-between mt-8">
                <div class="flex-1 mr-10">
                    <div class="w-full bg-gray-100 h-2 rounded-full overflow-hidden">
                        <div class="bg-primary h-2 rounded-full w-[25%] shadow-sm"></div>
                    </div>
                    <p class="text-[10px] text-primary mt-3 font-black uppercase tracking-widest">25% Complete</p>
                </div>
                <a href="${pageContext.request.contextPath}/student/module?phase=Phase2&title=${param.title}" 
                   class="px-8 py-3 text-xs font-black text-white bg-primary rounded-xl hover:bg-primary-dark shadow-xl shadow-primary/20 transition active:scale-95 uppercase tracking-widest">
                    Continue
                </a>
            </div>
        </div>

        <%-- Phase 3 --%>
        <div class="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm opacity-50 grayscale-[0.5]">
            <span class="text-[10px] text-gray-400 font-black uppercase tracking-widest">Phase 3 • Locked</span>
            <h3 class="text-xl text-gray-500 mt-1">React Framework</h3>
            <p class="text-sm text-gray-400 mt-1 font-medium">Components, Hooks, SPA development.</p>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/views/components/student_footer.jsp" />

<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('title')) {
            Swal.fire({
                title: 'Enrollment Complete!',
                text: 'You have successfully enrolled in the ' + urlParams.get('title') + ' path.',
                icon: 'success',
                confirmButtonColor: '#4913ec',
                customClass: {
                    popup: 'rounded-[32px]',
                    confirmButton: 'rounded-xl px-8 py-3 font-bold'
                }
            });
        }
    }
</script>

</body>
</html>