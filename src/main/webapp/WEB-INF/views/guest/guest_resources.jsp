<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Learning Resources Hub - PathPilot</title>

    <link href="https://fonts.googleapis.com" rel="preconnect"/>
    <link crossorigin="" href="https://fonts.gstatic.com" rel="preconnect"/>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Poppins:wght@500;600;700;800&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <script src="https://cdn.tailwindcss.com?plugins=forms"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        'primary': '#4913ec',
                        'primary-dark': '#3a0fb5'
                    },
                    fontFamily: {
                        'sans': ["'Plus Jakarta Sans'", 'sans-serif'],
                        'heading': ['Poppins', 'sans-serif']
                    }
                }
            }
        }
    </script>
    <style type="text/tailwindcss">
        body { font-family: 'Plus Jakarta Sans', sans-serif; }
        h1, h2, h3, h4, .font-heading { font-family: 'Poppins', sans-serif; }
        .filter-btn.active {
            @apply bg-primary text-white shadow-lg shadow-primary/20 border-primary !important;
        }
    </style>
</head>
<body class="bg-[#f8f9fc] antialiased">

    <jsp:include page="/WEB-INF/views/components/gnavbar.jsp"/>

    <header class="max-w-7xl mx-auto px-6 pt-16 pb-8">
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div>
                <h1 class="text-4xl font-bold text-gray-900 mb-4 tracking-tight">Learning Resources Hub</h1>
                <p class="text-gray-500 max-w-2xl leading-relaxed">Browse live published career paths and preview what you can unlock after signing in.</p>
            </div>
        </div>

        <div class="mt-12 flex flex-col md:flex-row gap-4 items-center">
            <div class="relative flex-grow max-w-md w-full">
                <span class="material-icons-round absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">search</span>
                <input type="text" id="resourceSearch" onkeyup="applyFilters()" placeholder="Search React, Docker, Security..." class="w-full pl-12 pr-4 py-3.5 bg-white border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-primary/10 focus:border-primary transition-all shadow-sm">
            </div>

            <div class="flex gap-2 overflow-x-auto pb-2 w-full md:w-auto">
                <button onclick="setCategoryFilter('all', this)" class="filter-btn active bg-white text-gray-600 border border-gray-200 px-6 py-2.5 rounded-full text-sm font-medium hover:bg-gray-50 transition-all whitespace-nowrap">All Domains</button>
                <button onclick="setCategoryFilter('development', this)" class="filter-btn bg-white text-gray-600 border border-gray-200 px-6 py-2.5 rounded-full text-sm font-medium hover:bg-gray-50 transition-all whitespace-nowrap">Development</button>
                <button onclick="setCategoryFilter('cloud', this)" class="filter-btn bg-white text-gray-600 border border-gray-200 px-6 py-2.5 rounded-full text-sm font-medium hover:bg-gray-50 transition-all whitespace-nowrap">Cloud</button>
                <button onclick="setCategoryFilter('security', this)" class="filter-btn bg-white text-gray-600 border border-gray-200 px-6 py-2.5 rounded-full text-sm font-medium hover:bg-gray-50 transition-all whitespace-nowrap">Cyber Security</button>
            </div>
        </div>
    </header>

    <main class="max-w-7xl mx-auto px-6 py-12">
        <div id="resourceGrid" class="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            <c:forEach var="path" items="${careerPaths}">
                <c:set var="bgColor" value="bg-[#2eb086]"/>
                <c:set var="iconClass" value="fa-solid fa-code"/>
                <c:set var="categoryKey" value="development"/>
                <c:set var="badgeLabel" value="Development"/>

                <c:if test="${path.category == 'Cloud Computing' || path.category == 'Cloud'}">
                    <c:set var="bgColor" value="bg-sky-600"/>
                    <c:set var="iconClass" value="fa-solid fa-cloud"/>
                    <c:set var="categoryKey" value="cloud"/>
                    <c:set var="badgeLabel" value="Cloud"/>
                </c:if>

                <c:if test="${path.category == 'Cyber Security' || path.category == 'Security'}">
                    <c:set var="bgColor" value="bg-red-600"/>
                    <c:set var="iconClass" value="fa-solid fa-shield-halved"/>
                    <c:set var="categoryKey" value="security"/>
                    <c:set var="badgeLabel" value="Cyber Security"/>
                </c:if>

                <div class="resource-card bg-white rounded-[2.5rem] overflow-hidden shadow-sm border border-gray-100 hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group"
                     data-category="${categoryKey}"
                     data-title="${path.title}"
                     data-description="${path.description}">
                    <div class="h-52 ${bgColor} flex items-center justify-center p-8 relative overflow-hidden">
                        <div class="absolute inset-0 bg-gradient-to-br from-white/10 to-transparent"></div>
                        <span class="absolute top-4 left-4 bg-white/20 backdrop-blur-md text-white text-[10px] font-black px-3 py-1 rounded-full uppercase">${badgeLabel}</span>
                        <div class="text-center text-white relative z-10">
                            <i class="${iconClass} text-5xl mb-3"></i>
                            <h4 class="font-bold text-xl uppercase tracking-tighter">${not empty path.level ? path.level : 'Published Path'}</h4>
                        </div>
                    </div>
                    <div class="p-8">
                        <h3 class="resource-title font-bold text-lg text-gray-900 mb-2">${path.title}</h3>
                        <p class="text-sm text-gray-500 leading-relaxed min-h-[3rem]">${path.description}</p>
                        <div class="flex items-center justify-between pt-6 border-t border-gray-50 mt-4">
                            <span class="text-xs text-gray-400 font-bold uppercase tracking-widest">${path.status}</span>
                            <a href="<%=request.getContextPath()%>/guest/course_detail?title=${path.title}" class="text-primary font-bold text-sm hover:underline">Quick View</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div id="noResults" class="hidden text-center py-20">
            <div class="w-20 h-20 bg-white rounded-full flex items-center justify-center mx-auto mb-6 shadow-sm border border-gray-100">
                <span class="material-icons-round text-4xl text-gray-200">search_off</span>
            </div>
            <h3 class="text-xl font-bold text-gray-900">No paths found</h3>
            <p class="text-gray-500 mt-2">Try another keyword or browse a different domain.</p>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/guest_footer.jsp" />

    <script>
        let activeCategory = 'all';

        function setCategoryFilter(category, btn) {
            activeCategory = category;
            document.querySelectorAll('.filter-btn').forEach(b => {
                b.classList.remove('active', 'bg-primary', 'text-white');
            });
            btn.classList.add('active', 'bg-primary', 'text-white');
            applyFilters();
        }

        function applyFilters() {
            const query = document.getElementById('resourceSearch').value.toLowerCase();
            const cards = document.querySelectorAll('.resource-card');
            let visibleCount = 0;

            cards.forEach(card => {
                const title = (card.getAttribute('data-title') || '').toLowerCase();
                const description = (card.getAttribute('data-description') || '').toLowerCase();
                const category = card.getAttribute('data-category');
                const matchesCategory = activeCategory === 'all' || category === activeCategory;
                const matchesSearch = title.includes(query) || description.includes(query);

                if (matchesCategory && matchesSearch) {
                    card.style.display = 'block';
                    visibleCount++;
                } else {
                    card.style.display = 'none';
                }
            });

            const noResults = document.getElementById('noResults');
            if (visibleCount === 0) {
                noResults.classList.remove('hidden');
            } else {
                noResults.classList.add('hidden');
            }
        }

        applyFilters();
    </script>
</body>
</html>
