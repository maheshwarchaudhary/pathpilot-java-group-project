<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Unified Role-Based Sidebar: STUDENT, USER (Mentor), ADMIN --%>
<%
    String currentUri = request.getServletPath();
    String role = session != null && session.getAttribute("role") != null
            ? String.valueOf(session.getAttribute("role")).toUpperCase()
            : "STUDENT";
    
    boolean isStudent = "STUDENT".equals(role);
    boolean isMentor = "USER".equals(role);
    boolean isAdmin = "ADMIN".equals(role);
%>

<aside class="flex flex-col justify-between h-screen sticky top-0 bg-white border-r border-gray-100 p-4 transition-all duration-300 font-['Plus_Jakarta_Sans']
             w-20 lg:w-64">
    
    <div>
        <!-- Logo Section -->
        <div class="flex items-center gap-3 mb-10 px-2">
            <div class="w-10 h-10 rounded-xl bg-indigo-600 flex-shrink-0 flex items-center justify-center text-white shadow-md">
                <span class="material-icons-round text-lg">rocket_launch</span>
            </div>
            <span class="text-xl font-bold tracking-tight text-gray-800 hidden lg:block">
                PathPilot
                <% if (isAdmin) { %>
                    <span class="text-[10px] bg-indigo-50 text-indigo-600 px-1.5 py-0.5 rounded ml-1 uppercase">Admin</span>
                <% } else if (isMentor) { %>
                    <span class="text-[10px] bg-indigo-50 text-indigo-600 px-1.5 py-0.5 rounded ml-1 uppercase">Mentor</span>
                <% } %>
            </span>
        </div>

        <!-- Navigation Menu -->
        <nav class="space-y-2">
            
            <!-- STUDENT LINKS -->
            <% if (isStudent) { %>
                <!-- My Profile -->
                <a href="<%=request.getContextPath()%>/student/profile" title="My Profile"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("profile") && !currentUri.contains("setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">person_outline</span>
                    <span class="hidden lg:block whitespace-nowrap">My Profile</span>
                </a>

                <!-- Certificates -->
                <a href="<%=request.getContextPath()%>/student/certificates" title="Certificates"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("certificates") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">workspace_premium</span>
                    <span class="hidden lg:block whitespace-nowrap">Certificates</span>
                </a>

                <!-- Happy Soul -->
                <a href="<%=request.getContextPath()%>/student/quotes" title="Happy Soul Quotes"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("quotes") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">auto_awesome</span>
                    <span class="hidden lg:block whitespace-nowrap">Happy Soul</span>
                </a>

                <!-- Settings -->
                <a href="<%=request.getContextPath()%>/student/settings" title="Settings"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">settings</span>
                    <span class="hidden lg:block whitespace-nowrap">Settings</span>
                </a>
            <% } %>

            <!-- MENTOR (USER) LINKS -->
            <% if (isMentor) { %>
                <!-- Dashboard -->
                <a href="<%=request.getContextPath()%>/user/profile" title="Dashboard"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("profile") && !currentUri.contains("setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">grid_view</span>
                    <span class="hidden lg:block whitespace-nowrap">Dashboard</span>
                </a>

                <!-- My Roadmaps -->
                <a href="<%=request.getContextPath()%>/user/career-mgmt" title="My Roadmaps"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("career") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">alt_route</span>
                    <span class="hidden lg:block whitespace-nowrap">My Roadmaps</span>
                </a>

                <!-- Learners -->
                <a href="<%=request.getContextPath()%>/user/learner-mgmt" title="Learners"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("learner-mgmt") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">group</span>
                    <span class="hidden lg:block whitespace-nowrap">Learners</span>
                </a>

                <!-- Certificates -->
                <a href="<%=request.getContextPath()%>/user/certificates" title="Certificates"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("certificates") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">verified</span>
                    <span class="hidden lg:block whitespace-nowrap">Certificates</span>
                </a>

                <!-- Settings -->
                <a href="<%=request.getContextPath()%>/user/settings" title="Settings"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">settings</span>
                    <span class="hidden lg:block whitespace-nowrap">Settings</span>
                </a>
            <% } %>

            <!-- ADMIN LINKS -->
            <% if (isAdmin) { %>
                <!-- Dashboard -->
                <a href="<%=request.getContextPath()%>/admin/dashboard" title="Admin Dashboard"
                   class="flex items-center gap-4 px-3 py-3 rounded-2xl font-semibold transition
                   <%= currentUri.contains("dashboard") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">dashboard</span>
                    <span class="hidden lg:block whitespace-nowrap">Dashboard</span>
                </a>

                <!-- Manage Users -->
                <a href="<%=request.getContextPath()%>/admin/users" title="Manage Users"
                   class="flex items-center gap-4 px-3 py-3 rounded-2xl font-semibold transition
                   <%= currentUri.contains("users") && !currentUri.contains("career") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">people</span>
                    <span class="hidden lg:block whitespace-nowrap">Manage Users</span>
                </a>

                  <!-- My Roadmaps -->
                <a href="<%=request.getContextPath()%>/user/career-mgmt" title="My Roadmaps"
                   class="flex items-center gap-4 px-3 py-3 transition rounded-2xl font-semibold 
                   <%= currentUri.contains("career") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">alt_route</span>
                    <span class="hidden lg:block whitespace-nowrap">Carrer Paths</span>
                </a>


                <!-- Settings -->
                <a href="<%=request.getContextPath()%>/admin/settings" title="System Settings"
                   class="flex items-center gap-4 px-3 py-3 rounded-2xl font-semibold transition
                   <%= currentUri.contains("setting") ? "bg-indigo-50 text-indigo-600" : "text-gray-500 hover:bg-gray-50 hover:text-indigo-600" %>">
                    <span class="material-icons-round text-2xl flex-shrink-0">settings</span>
                    <span class="hidden lg:block whitespace-nowrap">Settings</span>
                </a>
            <% } %>

        </nav>
    </div>

    <!-- Bottom Actions -->
    <div class="space-y-2 border-t border-gray-100 pt-6">
        <!-- Back/Home Button -->
        <a href="<%=request.getContextPath()%><%= isStudent ? "/student/home" : isMentor ? "/user/UserHome" : "/admin/dashboard" %>" 
           title="<%= isStudent ? "Back to Learning" : "Back Home" %>"
           class="flex items-center gap-4 px-3 py-3 transition font-bold text-gray-400 hover:text-indigo-600">
            <span class="material-icons-round text-2xl flex-shrink-0"><%= isStudent ? "home" : "arrow_back" %></span>
            <span class="hidden lg:block text-xs uppercase tracking-widest whitespace-nowrap"><%= isStudent ? "Portal Home" : "Back Home" %></span>
        </a>

        <!-- Logout Button -->
        <a href="<%=request.getContextPath()%>/logout" title="Logout"
           class="flex items-center gap-4 px-3 py-3 transition font-bold text-gray-400 hover:text-red-500">
            <span class="material-icons-round text-2xl flex-shrink-0">logout</span>
            <span class="hidden lg:block text-xs uppercase tracking-widest whitespace-nowrap">Logout</span>
        </a>
    </div>

</aside>
