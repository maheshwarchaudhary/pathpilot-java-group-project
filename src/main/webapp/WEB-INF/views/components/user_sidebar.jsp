<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Logic: Automatic collapse on mobile/small screens using Tailwind breakpoints --%>
<% String currentUri = request.getServletPath(); %>

<aside class="flex flex-col justify-between h-screen sticky top-0 bg-white border-r border-gray-100 p-4 transition-all duration-300 font-['Plus_Jakarta_Sans']
             w-20 lg:w-64"> <div>
        <div class="flex items-center gap-3 mb-10 px-2">
            <div class="w-10 h-10 rounded-xl bg-indigo-600 flex-shrink-0 flex items-center justify-center text-white shadow-md">
                <span class="material-icons-round text-lg">rocket_launch</span>
            </div>
            <span class="text-xl font-bold tracking-tight text-gray-800 hidden lg:block">PathPilot</span>
        </div>

        <nav class="space-y-2">
            <a href="<%=request.getContextPath()%>/user/profile" title="Dashboard"
               class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
               <%= currentUri.contains("profile") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                <span class="material-icons-round text-2xl flex-shrink-0">grid_view</span>
                <span class="hidden lg:block whitespace-nowrap">Dashboard</span>
            </a>

            <a href="<%=request.getContextPath()%>/user/career-mgmt" title="My Roadmaps"
               class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
               <%= currentUri.contains("mgmt") && currentUri.contains("career") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                <span class="material-icons-round text-2xl flex-shrink-0">alt_route</span>
                <span class="hidden lg:block whitespace-nowrap">My Roadmaps</span>
            </a>

            <a href="<%=request.getContextPath()%>/user/learner-mgmt" title="Learners"
               class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
               <%= currentUri.contains("learner-mgmt") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                <span class="material-icons-round text-2xl flex-shrink-0">group</span>
                <span class="hidden lg:block whitespace-nowrap">Learners</span>
            </a>

            <a href="<%=request.getContextPath()%>/user/certificates" title="Certificates"
               class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
               <%= currentUri.contains("user_certificates") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                <span class="material-icons-round text-2xl flex-shrink-0">verified</span>
                <span class="hidden lg:block whitespace-nowrap">Certificates</span>
            </a>

            <a href="<%=request.getContextPath()%>/user/settings" title="Settings"
               class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
               <%= currentUri.contains("user_setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                <span class="material-icons-round text-2xl flex-shrink-0">settings</span>
                <span class="hidden lg:block whitespace-nowrap">Settings</span>
            </a>
        </nav>
    </div>

    <div class="space-y-2 border-t border-gray-100 pt-6">
        <a href="<%=request.getContextPath()%>/user/UserHome" title="Back Home"
           class="flex items-center gap-4 px-3 py-3 transition font-bold text-gray-400 hover:text-indigo-600">
            <span class="material-icons-round text-2xl flex-shrink-0">arrow_back</span>
            <span class="hidden lg:block text-xs uppercase tracking-widest whitespace-nowrap">Back Home</span>
        </a>

        <a href="<%=request.getContextPath()%>/logout" title="Logout"
           class="flex items-center gap-4 px-3 py-3 transition font-bold text-gray-400 hover:text-red-500">
            <span class="material-icons-round text-2xl flex-shrink-0">logout</span>
            <span class="hidden lg:block text-xs uppercase tracking-widest whitespace-nowrap">Logout</span>
        </a>
    </div>
</aside>