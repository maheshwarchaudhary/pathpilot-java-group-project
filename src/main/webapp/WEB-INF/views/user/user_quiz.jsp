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
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Assessment - <%=phase%></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        body { 
            font-family: 'Plus Jakarta Sans', sans-serif; 
            background-color: #f8f9fc; /* PathPilot Original Light BG */
            min-height: 100vh;
        }

        /* 📻 Custom Radio Button (Square style from image but PathPilot colors) */
        input[type="radio"] {
            appearance: none;
            -webkit-appearance: none;
            width: 22px;
            height: 22px;
            border: 2px solid #e2e8f0;
            border-radius: 6px; 
            background: white;
            cursor: pointer;
            position: relative;
            outline: none;
            flex-shrink: 0;
            transition: all 0.2s ease;
        }

        input[type="radio"]:checked {
            border-color: #4913ec; /* Primary Indigo */
            background-color: #f5f3ff;
        }

        input[type="radio"]:checked::after {
            content: '\f00c';
            font-family: 'Font Awesome 5 Free';
            font-weight: 900;
            font-size: 11px;
            color: #4913ec;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .option-container {
            @apply flex items-center gap-4 py-4 px-6 rounded-2xl border-2 border-transparent hover:bg-white hover:shadow-md transition-all duration-200 cursor-pointer;
        }

        .option-container:hover input[type="radio"] {
            border-color: #4913ec;
        }

        .progress-bar-container {
            @apply w-full bg-gray-200 h-1.5 rounded-full overflow-hidden;
        }
    </style>
</head>

<body class="antialiased flex flex-col">

    <nav class="bg-white border-b border-gray-100 py-5 px-10 flex justify-between items-center shadow-sm">
        <div class="flex items-center gap-4">
            <div class="w-10 h-10 rounded-xl bg-[#4913ec] flex items-center justify-center text-white shadow-lg shadow-indigo-200">
                <i class="fas fa-graduation-cap"></i>
            </div>
            <div>
                <h1 class="text-lg font-800 text-gray-900 tracking-tight"><%=phase%> Assessment</h1>
                <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest"><%=title%></p>
            </div>
        </div>
        
        <div class="flex items-center gap-6">
            <div class="text-right hidden sm:block">
                <span id="qCountDisplay" class="text-[10px] font-black text-indigo-600 uppercase tracking-widest">Question 1 of 5</span>
                <div class="progress-bar-container mt-1 w-32">
                    <div id="progressBar" class="bg-[#4913ec] h-full w-0 transition-all duration-700"></div>
                </div>
            </div>
            <button onclick="history.back()" class="text-gray-400 hover:text-red-500 transition-colors">
                <i class="fas fa-times-circle text-xl"></i>
            </button>
        </div>
    </nav>

    <main class="flex-grow flex items-center justify-center px-6 py-12">
        <div id="quizBox" class="max-w-2xl w-full">
            
            <div class="mb-12">
                <span class="inline-block px-4 py-1.5 rounded-full bg-indigo-50 text-[#4913ec] text-[10px] font-black uppercase tracking-widest mb-4">Knowledge Check</span>
                <h2 id="qText" class="text-3xl font-800 text-gray-900 leading-tight tracking-tight">
                    Loading your question...
                </h2>
            </div>

            <div id="optionsBox" class="flex flex-col gap-2">
                </div>

            <div class="mt-16 pt-8 border-t border-gray-100 flex justify-between items-center">
                <button onclick="confirmExit()" class="text-gray-400 hover:text-gray-600 text-xs font-bold uppercase tracking-widest transition-all">
                    Quit Assessment
                </button>
                
                <button id="nextBtn" onclick="submitAnswer()" class="bg-[#4913ec] hover:bg-[#3a0fb5] text-white px-12 py-4 rounded-2xl font-bold text-sm shadow-xl shadow-indigo-200 transition-all active:scale-95 flex items-center gap-3">
                    Submit & Next <i class="fas fa-arrow-right text-[10px]"></i>
                </button>
            </div>
        </div>
    </main>

    <script>
        const quizData = {
            "Phase 1": [
                { q: "Which tool is used as a primary code editor?", o: ["Visual Studio Code", "Notepad++", "MS PowerPoint", "Google Sheets"], a: 0 },
                { q: "What is the default port for Live Server?", o: ["Port 3000", "Port 5000", "Port 5500", "Port 8080"], a: 2 },
                { q: "Which symbol is used for IDs in CSS?", o: ["The Period (.)", "The Hash (#)", "The Ampersand (&)", "The Asterisk (*)"], a: 1 },
                { q: "Which command copies a repository?", o: ["git push origin", "git clone [url]", "git commit -m", "git init"], a: 1 },
                { q: "Standard HTML tag for main headings?", o: ["<paragraph>", "<heading-6>", "<h1>", "<section>"], a: 2 }
            ],
            "Phase 2": [
                { q: "Keyword to define a constant in JS?", o: ["var", "let", "const", "static"], a: 2 },
                { q: "Event listener for mouse clicks?", o: ["onhover", "onclick", "onscroll", "onchange"], a: 1 },
                { q: "Data type for True/False?", o: ["String", "Integer", "Boolean", "Object"], a: 2 },
                { q: "Result of 10 + '5' in JS?", o: ["15", "105", "NaN", "Error"], a: 1 },
                { q: "Function to log data?", o: ["print()", "console.log()", "display()", "write()"], a: 1 }
            ]
        };

        const currentPhase = "<%=phase%>";
        const questions = quizData[currentPhase] || quizData["Phase 1"];
        let currentIdx = 0;
        let score = 0;
        let selectedOption = -1;

        function loadQuestion() {
            const q = questions[currentIdx];
            selectedOption = -1;
            
            document.getElementById('qCountDisplay').innerText = "Question " + (currentIdx + 1) + " of 5";
            document.getElementById('qText').innerText = q.q;
            document.getElementById('progressBar').style.width = ((currentIdx) / 5 * 100) + "%";

            const optionsBox = document.getElementById('optionsBox');
            let html = "";
            
            q.o.forEach((opt, i) => {
                html += '<label class="option-container group">';
                html += '   <input type="radio" name="quizOpt" value="' + i + '" onclick="setSelection(' + i + ')">';
                html += '   <span class="text-xl font-medium text-gray-600 group-hover:text-gray-900 transition-colors">' + opt + '</span>';
                html += '</label>';
            });
            
            optionsBox.innerHTML = html;
        }

        function setSelection(i) {
            selectedOption = i;
        }

        function submitAnswer() {
            if (selectedOption === -1) {
                Swal.fire({ 
                    text: "Please select an answer", 
                    icon: 'warning',
                    confirmButtonColor: '#4913ec'
                });
                return;
            }

            if (selectedOption === questions[currentIdx].a) {
                score++;
            }

            currentIdx++;
            if (currentIdx < 5) {
                loadQuestion();
            } else {
                finishQuiz();
            }
        }

        function finishQuiz() {
            document.getElementById('progressBar').style.width = "100%";
            Swal.fire({
                title: 'Assessment Complete!',
                text: 'Your Score: ' + score + '/5',
                icon: score >= 3 ? 'success' : 'error',
                confirmButtonText: score >= 3 ? 'Finish Milestone' : 'Try Again',
                confirmButtonColor: '#4913ec'
            }).then((result) => {
                if(score >= 3) {
                    window.location.href = "<%=request.getContextPath()%>/user/progress?title=<%=title%>&status=complete";
                } else {
                    location.reload();
                }
            });
        }

        function confirmExit() {
            Swal.fire({
                title: 'Quit Assessment?',
                text: "Your progress in this test will be lost.",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#ef4444',
                confirmButtonText: 'Yes, Quit'
            }).then((r) => { if(r.isConfirmed) history.back(); });
        }

        loadQuestion();
    </script>
</body>
</html>