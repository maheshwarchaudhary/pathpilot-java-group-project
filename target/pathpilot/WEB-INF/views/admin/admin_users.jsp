<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // 🔐 CACHE CONTROL
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // 🔐 ADMIN SESSION CHECK
    if(session == null || session.getAttribute("role") == null || 
       !"ADMIN".equals(session.getAttribute("role"))){
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - PathPilot</title>
    
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Poppins:wght@600;700;800&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet"/>
    
    <script src="https://cdn.tailwindcss.com?plugins=forms"></script>

    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: "#4913ec",
                        "primary-dark": "#3a0fb5",
                        "bg-light": "#f8f9fc"
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
        body { font-family: 'Plus Jakarta Sans', sans-serif; }
        h1, h2, h3, .font-heading { font-family: 'Poppins', sans-serif; }
        
        .action-btn { @apply p-2.5 rounded-xl transition-all duration-200 shadow-sm active:scale-95; }
        .modal-input { @apply w-full bg-gray-50 border border-transparent rounded-2xl px-5 py-4 focus:ring-2 focus:ring-primary/20 transition-all outline-none font-medium text-sm; }
        .section-label { @apply text-[10px] font-black uppercase text-gray-400 tracking-widest block mb-1 ml-1; }
        
        /* Validation Styles */
        .input-error { @apply border-red-500 bg-red-50/50 focus:ring-red-200 !important; }
        .error-msg { @apply text-[9px] font-bold text-red-500 uppercase tracking-tight ml-2 mt-1.5 hidden; }
    </style>
</head>

<body class="bg-bg-light antialiased overflow-hidden">
    <div class="flex h-screen">
        <!-- ✅ SIDEBAR -->
        <jsp:include page="/WEB-INF/views/components/sidebar.jsp" />

        <div class="flex-1 flex flex-col overflow-hidden">
            <!-- ✅ HEADER -->
            <header class="h-20 bg-white/80 backdrop-blur-md border-b border-gray-100 flex items-center justify-between px-12 sticky top-0 z-10">
                <div>
                    <nav class="text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1 flex items-center">
                        <span>Admin</span>
                        <span class="material-icons-round text-xs mx-2">chevron_right</span>
                        <span class="text-primary font-bold">Manage Users</span>
                    </nav>
                    <h1 class="text-xl font-800 text-gray-900 tracking-tight">Identity Management</h1>
                </div>
            </header>

            <main class="flex-1 overflow-y-auto p-12">
                <div class="flex justify-between items-center mb-10">
                    <h1 class="text-2xl font-800 text-gray-900 tracking-tight">System Users</h1>
                    <button onclick="openAddModal()"
                       class="px-8 py-4 bg-primary text-white rounded-2xl font-bold text-sm shadow-xl shadow-primary/20 hover:bg-primary-dark transition-all active:scale-95 flex items-center gap-2">
                        <span class="material-icons-round text-base">person_add</span> Add New User
                    </button>
                </div>

                <!-- ✅ USER TABLE -->
                <div class="bg-white rounded-[2.5rem] border border-gray-100 shadow-sm overflow-hidden">
                    <table class="w-full text-sm">
                        <thead class="bg-gray-50/50 text-gray-400 border-b border-gray-50">
                            <tr>
                                <th class="px-8 py-6 text-left text-[10px] font-black uppercase tracking-widest">User Details</th>
                                <th class="px-8 py-6 text-left text-[10px] font-black uppercase tracking-widest">Role</th>
                                <th class="px-8 py-6 text-left text-[10px] font-black uppercase tracking-widest">Status</th>
                                <th class="px-8 py-6 text-center text-[10px] font-black uppercase tracking-widest">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="usersTableBody" class="divide-y divide-gray-50">
                            <tr>
                                <td colspan="4" class="px-8 py-12 text-center">
                                    <div class="flex items-center justify-center gap-3">
                                        <div class="w-4 h-4 bg-primary rounded-full animate-bounce"></div>
                                        <span class="text-gray-400">Loading users...</span>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    <!-- ✅ ADD USER MODAL -->
    <div id="addModal" class="fixed inset-0 bg-gray-900/40 backdrop-blur-sm hidden items-center justify-center z-50 p-4">
        <div class="bg-white w-full max-w-2xl rounded-[3rem] p-12 shadow-2xl border border-gray-100 animate-in zoom-in duration-300 max-h-[95vh] overflow-y-auto">
            <h2 class="text-2xl font-800 text-gray-900 mb-2 tracking-tight">Create New User</h2>
            <p class="text-gray-400 text-xs font-medium mb-10">Assign credentials and contact details directly.</p>
            
            <form id="addForm" novalidate>
                <div class="grid grid-cols-2 gap-x-6 gap-y-4">
                    <div class="col-span-2">
                        <label class="section-label">Full Name</label>
                        <input id="aName" type="text" placeholder="e.g. Rahul Sharma" class="modal-input" oninput="clearError('aName', 'aNameError')">
                        <span id="aNameError" class="error-msg">Please enter full name (min 3 chars)</span>
                    </div>
                    <div class="col-span-1">
                        <label class="section-label">Email Address</label>
                        <input id="aEmail" type="email" placeholder="email@example.com" class="modal-input" oninput="clearError('aEmail', 'aEmailError')">
                        <span id="aEmailError" class="error-msg">Valid email is required</span>
                    </div>
                    <div class="col-span-1">
                        <label class="section-label">Contact Number</label>
                        <input id="aPhone" type="text" placeholder="+91 00000 00000" class="modal-input" oninput="clearError('aPhone', 'aPhoneError')">
                        <span id="aPhoneError" class="error-msg">Enter 10-digit mobile number</span>
                    </div>
                    <div class="col-span-1">
                        <label class="section-label">Initial Password</label>
                        <input id="aPass" type="password" placeholder="••••••••" class="modal-input" oninput="clearError('aPass', 'aPassError')">
                        <span id="aPassError" class="error-msg">Password required (min 6 chars)</span>
                    </div>
                    <div class="col-span-1">
                        <label class="section-label">Assign Role</label>
                        <select id="aRole" class="modal-input cursor-pointer">
                            <option value="student">Student</option>
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                </div>
                <div class="flex justify-end gap-4 mt-12">
                    <button type="button" onclick="closeAddModal()" class="px-8 py-3 rounded-2xl font-bold text-xs text-gray-400 uppercase tracking-widest">Cancel</button>
                    <button type="button" onclick="handleAddUser()" class="px-10 py-4 bg-primary text-white rounded-2xl font-bold text-sm shadow-xl shadow-primary/20 hover:bg-primary-dark transition-all">
                        Create Account
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- ✅ EDIT MODAL -->
    <div id="editModal" class="fixed inset-0 bg-gray-900/40 backdrop-blur-sm hidden items-center justify-center z-50 p-4">
        <div class="bg-white w-full max-w-2xl rounded-[3rem] p-12 shadow-2xl border border-gray-100 animate-in zoom-in duration-200 max-h-[95vh] overflow-y-auto">
            <h2 class="text-2xl font-800 text-gray-900 mb-2 tracking-tight text-primary">Override User Account</h2>
            <p class="text-gray-400 text-xs font-medium mb-10">Administrative direct access to change passwords and contact numbers.</p>
            
            <form id="editForm" novalidate>
                <input type="hidden" id="editUserId">
                <div class="grid grid-cols-2 gap-x-6 gap-y-4">
                    <div class="col-span-2 bg-indigo-50/50 p-6 rounded-3xl border border-indigo-100 mb-2">
                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label class="section-label">Full Name</label>
                                <input id="eName" type="text" class="modal-input border-white shadow-sm" oninput="clearError('eName', 'eNameError')">
                                <span id="eNameError" class="error-msg">Name is required</span>
                            </div>
                            <div>
                                <label class="section-label">Email (Read Only)</label>
                                <input id="eEmail" type="email" class="modal-input border-white shadow-sm opacity-60" readonly>
                            </div>
                        </div>
                    </div>

                    <div>
                        <label class="section-label text-red-500 font-black">Security Reset (New Password)</label>
                        <input id="ePass" type="password" placeholder="Leave blank to keep current" class="modal-input border-red-100 focus:ring-red-100" oninput="clearError('ePass', 'ePassError')">
                        <span id="ePassError" class="error-msg">Minimum 6 characters required</span>
                    </div>
                    <div>
                        <label class="section-label">Direct Contact Number</label>
                        <input id="ePhone" type="text" class="modal-input" oninput="clearError('ePhone', 'ePhoneError')">
                        <span id="ePhoneError" class="error-msg">Enter valid contact number</span>
                    </div>
                    <div>
                        <label class="section-label">System Role</label>
                        <select id="eRole" class="modal-input cursor-pointer">
                            <option value="student">Student</option>
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                    <div>
                        <label class="section-label">Account Status</label>
                        <select id="eStatus" class="modal-input cursor-pointer">
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                            <option value="Banned">Banned</option>
                        </select>
                    </div>
                </div>
                <div class="flex justify-end gap-4 mt-12">
                    <button type="button" onclick="closeEdit()" class="px-8 py-3 rounded-2xl font-bold text-xs text-gray-400 uppercase tracking-widest">Cancel</button>
                    <button type="button" onclick="handleUpdate()" class="px-10 py-4 bg-primary text-white rounded-2xl font-bold text-sm shadow-xl shadow-primary/20 hover:bg-primary-dark transition-all">
                        Update Identity
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- ✅ VIEW MODAL (Logic Unchanged) -->
    <div id="viewModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-md hidden items-center justify-center z-50 p-4">
        <div class="bg-white w-full max-w-md rounded-[3rem] shadow-2xl overflow-hidden border border-gray-100 animate-in zoom-in duration-200">
            <div class="bg-primary p-10 text-white text-center">
                <div class="w-16 h-16 bg-white/20 rounded-2xl mx-auto mb-4 flex items-center justify-center backdrop-blur-md">
                    <span class="material-icons-round text-3xl text-white">person</span>
                </div>
                <h2 class="text-2xl font-800 tracking-tight">User Profile</h2>
                <p class="text-white/60 text-[10px] font-black uppercase tracking-widest mt-2" id="vNameHeader"></p>
            </div>
            <div class="p-10 space-y-6">
                <div class="grid grid-cols-1 gap-6">
                    <div>
                        <span class="section-label">Full Name & System ID</span>
                        <p class="font-800 text-gray-900"><span id="vName"></span> • <span id="vID" class="text-primary font-black"></span></p>
                    </div>
                    <div class="p-5 bg-gray-50 rounded-2xl border border-gray-100">
                        <span class="section-label">Confidential Contact Info</span>
                        <div class="space-y-2 mt-2">
                            <div class="flex items-center gap-3">
                                <span class="material-icons-round text-primary text-sm">alternate_email</span>
                                <span id="vEmail" class="font-700 text-gray-700 text-sm"></span>
                            </div>
                            <div class="flex items-center gap-3">
                                <span class="material-icons-round text-green-500 text-sm">call</span>
                                <span id="vPhoneView" class="font-900 text-gray-900 text-sm"></span>
                            </div>
                        </div>
                    </div>
                    <div>
                        <span class="section-label">Account Role</span>
                        <span id="vRole" class="text-[10px] font-black text-primary px-3 py-1 bg-indigo-50 rounded-lg uppercase inline-block mt-1"></span>
                    </div>
                </div>
                <button onclick="closeView()" class="w-full py-4 bg-gray-50 text-gray-400 rounded-2xl font-black text-[10px] uppercase tracking-widest hover:bg-gray-100 transition">Close Overlay</button>
            </div>
        </div>
    </div>

    <script>
        // ✅ UTILITY FUNCTIONS
        function showError(inputId, spanId) {
            document.getElementById(inputId).classList.add('input-error');
            document.getElementById(spanId).style.display = 'block';
        }

        function clearError(inputId, spanId) {
            document.getElementById(inputId).classList.remove('input-error');
            document.getElementById(spanId).style.display = 'none';
        }

        function resetFormErrors(formId) {
            const form = document.getElementById(formId);
            form.querySelectorAll('.modal-input').forEach(input => input.classList.remove('input-error'));
            form.querySelectorAll('.error-msg').forEach(span => span.style.display = 'none');
            form.reset();
        }

        // ✅ MODAL CONTROLS
        function openAddModal() {
            resetFormErrors('addForm');
            document.getElementById("addModal").classList.replace("hidden", "flex");
        }
        function closeAddModal() {
            document.getElementById("addModal").classList.replace("flex", "hidden");
        }

        function openEdit(userId, name, email, role, status, phone) {
            resetFormErrors('editForm');
            document.getElementById("editUserId").value = userId;
            document.getElementById("eName").value = name;
            document.getElementById("eEmail").value = email;
            document.getElementById("eRole").value = role.toLowerCase();
            document.getElementById("eStatus").value = status;
            document.getElementById("ePhone").value = phone || '';
            document.getElementById("editModal").classList.replace("hidden", "flex");
        }
        function closeEdit() {
            document.getElementById("editModal").classList.replace("flex", "hidden");
        }

        function openView(name, email, role, status, id, phone) {
            document.getElementById("vNameHeader").innerText = "ID: " + id;
            document.getElementById("vName").innerText = name;
            document.getElementById("vEmail").innerText = email;
            document.getElementById("vID").innerText = id;
            document.getElementById("vRole").innerText = role;
            document.getElementById("vPhoneView").innerText = phone || 'N/A';
            document.getElementById("viewModal").classList.replace("hidden", "flex");
        }
        function closeView() { 
            document.getElementById("viewModal").classList.replace("flex", "hidden"); 
        }

        // ✅ LOAD USERS ON PAGE LOAD
        window.addEventListener('DOMContentLoaded', function() {
            loadUsers();
        });

        // ✅ LOAD USERS FROM API
        function loadUsers() {
            fetch("<%=request.getContextPath()%>/admin/api/users")
                .then(res => {
                    console.log("📡 API Response Status:", res.status);
                    return res.json();
                })
                .then(data => {
                    console.log("📊 API Response Data:", data);
                    if (data.success && data.data) {
                        console.log("✅ Data received, rendering table...");
                        renderUsersTable(data.data);
                        attachTableEventListeners();
                    } else {
                        console.error("Failed to load users:", data);
                        document.getElementById("usersTableBody").innerHTML = '<tr><td colspan="4" class="px-8 py-12 text-center text-gray-400">Failed to load users</td></tr>';
                    }
                })
                .catch(err => {
                    console.error("Error loading users:", err);
                    document.getElementById("usersTableBody").innerHTML = '<tr><td colspan="4" class="px-8 py-12 text-center text-gray-400">Error loading users</td></tr>';
                });
        }

        function renderUsersTable(users) {
            const tbody = document.getElementById("usersTableBody");
            
            if (!Array.isArray(users) || users.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" class="px-8 py-12 text-center text-gray-400">No users found</td></tr>';
                return;
            }

            console.log("🎨 Rendering " + users.length + " users");
            
            let html = '';
            
            for (let idx = 0; idx < users.length; idx++) {
                const user = users[idx];
                console.log("  User " + idx + ":", user);
                
                const name = user.name ? String(user.name).trim() : 'Unknown';
                const email = user.email ? String(user.email).trim() : 'N/A';
                const phone = user.phone ? String(user.phone).trim() : 'N/A';
                const role = user.role ? String(user.role).toLowerCase().trim() : 'student';
                const id = user.id || '?';
                const verified = user.verified === true || user.verified === 1;

                const initials = name.split(' ').map(n => n.charAt(0)).join('').substring(0, 2).toUpperCase() || 'U';
                
                const roleClass = role === 'admin' ? 'bg-red-50 text-red-600' : 
                                  role === 'user' ? 'bg-purple-50 text-purple-600' : 
                                  'bg-indigo-50 text-primary';
                const roleLabel = role === 'admin' ? 'Admin' : 
                                  role === 'user' ? 'User' : 'Student';
                
                const statusClass = verified ? 'bg-green-500' : 'bg-gray-400';
                const statusText = verified ? 'Active' : 'Inactive';
                const pulsing = verified ? 'animate-pulse' : '';
                const statusColor = verified ? 'text-green-600' : 'text-gray-400';

                html += '<tr class="hover:bg-gray-50/50 transition-colors">';
                html += '  <td class="px-8 py-6 flex items-center gap-4">';
                html += '    <div class="w-12 h-12 rounded-2xl bg-indigo-100 text-primary flex items-center justify-center font-bold text-sm">' + initials + '</div>';
                html += '    <div>';
                html += '      <p class="font-800 text-gray-900">' + name + '</p>';
                html += '      <p class="text-[10px] text-primary font-black uppercase tracking-widest">ID: ' + id + ' • ' + email + '</p>';
                html += '    </div>';
                html += '  </td>';
                html += '  <td class="px-8 py-6">';
                html += '    <span class="px-3 py-1 ' + roleClass + ' rounded-lg text-[9px] font-black uppercase tracking-widest w-fit">' + roleLabel + '</span>';
                html += '  </td>';
                html += '  <td class="px-8 py-6">';
                html += '    <div class="flex items-center gap-2">';
                html += '      <span class="w-2 h-2 ' + statusClass + ' rounded-full ' + pulsing + '"></span>';
                html += '      <span class="text-[10px] font-black uppercase tracking-widest ' + statusColor + '">' + statusText + '</span>';
                html += '    </div>';
                html += '  </td>';
                html += '  <td class="px-8 py-6">';
                html += '    <div class="flex justify-center gap-3">';
                html += '      <button class="action-btn bg-blue-50 text-blue-600 hover:bg-blue-100 btn-view" ';
                html += '        data-id="' + id + '" data-name="' + name + '" data-email="' + email + '" ';
                html += '        data-role="' + roleLabel + '" data-phone="' + phone + '">';
                html += '        <span class="material-icons-round text-lg">visibility</span>';
                html += '      </button>';
                html += '      <button class="action-btn bg-indigo-50 text-primary hover:bg-indigo-100 btn-edit" ';
                html += '        data-id="' + id + '" data-name="' + name + '" data-email="' + email + '" ';
                html += '        data-role="' + role + '" data-status="' + statusText + '" data-phone="' + phone + '">';
                html += '        <span class="material-icons-round text-lg">edit</span>';
                html += '      </button>';
                html += '      <button class="action-btn bg-red-50 text-red-600 hover:bg-red-100 btn-delete" ';
                html += '        data-id="' + id + '" data-name="' + name + '">';
                html += '        <span class="material-icons-round text-lg">delete</span>';
                html += '      </button>';
                html += '    </div>';
                html += '  </td>';
                html += '</tr>';
            }
            
            tbody.innerHTML = html;
            attachTableEventListeners();
            console.log("✅ Table rendered successfully with " + users.length + " rows");
        }

        function attachTableEventListeners() {
            // View button listeners
            document.querySelectorAll('.btn-view').forEach(btn => {
                btn.addEventListener('click', function() {
                    const name = this.dataset.name;
                    const email = this.dataset.email;
                    const role = this.dataset.role;
                    const phone = this.dataset.phone;
                    const id = this.dataset.id;
                    openView(name, email, role, '', id, phone);
                });
            });

            // Edit button listeners
            document.querySelectorAll('.btn-edit').forEach(btn => {
                btn.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const name = this.dataset.name;
                    const email = this.dataset.email;
                    const role = this.dataset.role;
                    const status = this.dataset.status;
                    const phone = this.dataset.phone;
                    openEdit(id, name, email, role, status, phone);
                });
            });

            // Delete button listeners
            document.querySelectorAll('.btn-delete').forEach(btn => {
                btn.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const name = this.dataset.name;
                    deleteUser(id, name);
                });
            });
        }

        // ✅ FORM VALIDATION LOGIC
        function handleAddUser() {
            const name = document.getElementById("aName").value.trim();
            const email = document.getElementById("aEmail").value.trim();
            const phone = document.getElementById("aPhone").value.trim();
            const pass = document.getElementById("aPass").value.trim();
            const role = document.getElementById("aRole").value || "student";
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            let isValid = true;

            if (name.length < 3) { showError("aName", "aNameError"); isValid = false; }
            else { clearError("aName", "aNameError"); }
            
            if (!emailRegex.test(email)) { showError("aEmail", "aEmailError"); isValid = false; }
            else { clearError("aEmail", "aEmailError"); }
            
            if (phone.length < 10) { showError("aPhone", "aPhoneError"); isValid = false; }
            else { clearError("aPhone", "aPhoneError"); }
            
            if (pass.length < 6) { showError("aPass", "aPassError"); isValid = false; }
            else { clearError("aPass", "aPassError"); }

            if (isValid) {
                // AJAX call to create user
                const formData = new FormData();
                formData.append("name", name);
                formData.append("email", email);
                formData.append("phone", phone);
                formData.append("password", pass);
                formData.append("role", role);

                fetch("<%=request.getContextPath()%>/admin/api/users", {
                    method: "POST",
                    body: formData
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        showSuccessToast("User created successfully");
                        closeAddModal();
                        loadUsers();
                    } else {
                        alert("Error: " + (data.message || "Failed to create user"));
                    }
                })
                .catch(err => {
                    console.error("Error:", err);
                    alert("Network error while creating user");
                });
            }
        }

        function handleUpdate() {
            const userId = document.getElementById("editUserId").value;
            const name = document.getElementById("eName").value.trim();
            const phone = document.getElementById("ePhone").value.trim();
            const pass = document.getElementById("ePass").value.trim();
            const role = document.getElementById("eRole").value || "student";
            const status = document.getElementById("eStatus").value || "Active";
            
            let isValid = true;

            if (name.length < 3) { showError("eName", "eNameError"); isValid = false; }
            else { clearError("eName", "eNameError"); }
            
            // Phone is optional - only validate if provided
            if (phone.length > 0 && phone.length < 10) { showError("ePhone", "ePhoneError"); isValid = false; }
            else { clearError("ePhone", "ePhoneError"); }
            
            if (pass.length > 0 && pass.length < 6) { showError("ePass", "ePassError"); isValid = false; }
            else { clearError("ePass", "ePassError"); }

            if (isValid) {
                // AJAX call to update user
                const formData = new FormData();
                formData.append("name", name);
                formData.append("phone", phone);
                if (pass) formData.append("password", pass);
                formData.append("role", role);
                formData.append("status", status);

                fetch("<%=request.getContextPath()%>/admin/api/users/" + userId, {
                    method: "PUT",
                    body: formData
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        showSuccessToast("User updated successfully");
                        closeEdit();
                        loadUsers();
                    } else {
                        alert("Error: " + (data.message || "Failed to update user"));
                    }
                })
                .catch(err => {
                    console.error("Error:", err);
                    alert("Network error while updating user");
                });
            }
        }

        function deleteUser(userId, userName) {
            if (!confirm(`Are you sure you want to delete ${userName}? This action cannot be undone.`)) {
                return;
            }

            fetch("<%=request.getContextPath()%>/admin/api/users/" + userId, {
                method: "DELETE"
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    showSuccessToast("User deleted successfully");
                    loadUsers();
                } else {
                    alert("Error: " + (data.message || "Failed to delete user"));
                }
            })
            .catch(err => {
                console.error("Error:", err);
                alert("Network error while deleting user");
            });
        }

        function showSuccessToast(message) {
            const toast = document.createElement('div');
            toast.className = 'fixed bottom-10 right-10 bg-green-500 text-white px-8 py-4 rounded-2xl shadow-2xl z-50 flex items-center gap-3';
            toast.innerHTML = `<span class="material-icons-round text-lg">check_circle</span><span>${message}</span>`;
            document.body.appendChild(toast);
            setTimeout(() => toast.remove(), 3000);
        }
    </script>
</body>
</html>