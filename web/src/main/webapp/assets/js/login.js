  const params = new URLSearchParams(window.location.search);
  const initialRole = params.get('role') || 'customer';
  const roleText = { customer: 'Khách hàng', shipper: 'Shipper', store: 'Cửa hàng' };
  document.getElementById('loginRole').value = initialRole;
  document.getElementById('registerRole').value = initialRole;

  function updateSubtitle() {
    document.getElementById('roleSubtitle').textContent = roleText[document.getElementById('loginRole').value] || 'Khách hàng';
  }
  updateSubtitle();

  function renderRegisterFields() {
    const role = document.getElementById('registerRole').value;
    let html = `
      <div class="form-group"><label>Email</label><input type="email" id="regEmail" placeholder="email@example.com" required></div>
      <div class="form-group"><label>Mật khẩu</label><input type="password" id="regPassword" placeholder="Tối thiểu 6 ký tự" minlength="6" required></div>`;
    if (role === 'customer') {
      html += `
        <div class="form-group"><label>Họ và tên</label><input type="text" id="regName" placeholder="Nguyễn Văn A" required></div>
        <div class="form-group"><label>Ngày sinh</label><input type="date" id="regDob" required></div>
        <div class="form-group"><label>Địa chỉ</label><input type="text" id="regAddress" placeholder="Số nhà, đường, quận..." required></div>
        <div class="form-group"><label>Số điện thoại</label><input type="tel" id="regPhone" placeholder="0912345678" pattern="[0-9]{10}" required></div>`;
    } else if (role === 'shipper') {
      html += `
        <div class="form-group"><label>Họ và tên</label><input type="text" id="regName" required></div>
        <div class="form-group"><label>Ngày sinh</label><input type="date" id="regDob" required></div>
        <div class="form-group"><label>Số điện thoại</label><input type="text" id="regPhone" required></div>
        <div class="form-group"><label>Phương tiện</label><input type="text" id="regVehicle" placeholder="Xe máy" required></div>`;
    } else {
      html += `
        <div class="form-group"><label>Tên cửa hàng</label><input type="text" id="regStoreName" required></div>
        <div class="form-group"><label>Địa chỉ</label><input type="text" id="regAddress" required></div>
        <div class="form-group"><label>Số điện thoại</label><input type="text" id="regPhone" required></div>`;
    }
    document.getElementById('registerFields').innerHTML = html;
  }
  renderRegisterFields();

  document.getElementById('loginRole').addEventListener('change', updateSubtitle);
  document.getElementById('registerRole').addEventListener('change', renderRegisterFields);
  document.getElementById('tabLogin').addEventListener('click', () => switchTab('login'));
  document.getElementById('tabRegister').addEventListener('click', () => switchTab('register'));

  function switchTab(tab) {
    document.getElementById('tabLogin').classList.toggle('active', tab === 'login');
    document.getElementById('tabRegister').classList.toggle('active', tab === 'register');
    document.getElementById('loginPanel').classList.toggle('active', tab === 'login');
    document.getElementById('registerPanel').classList.toggle('active', tab === 'register');
    document.getElementById('loginError').style.display = 'none';
    document.getElementById('registerError').style.display = 'none';
    document.getElementById('registerSuccess').style.display = 'none';
  }

  function getUsers(role) { return JSON.parse(localStorage.getItem('users_' + role) || '[]'); }
  function saveUsers(role, users) { localStorage.setItem('users_' + role, JSON.stringify(users)); }

  document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const role = document.getElementById('registerRole').value;
    const errorEl = document.getElementById('registerError');
    const successEl = document.getElementById('registerSuccess');
    errorEl.style.display = 'none';
    successEl.style.display = 'none';
    const email = document.getElementById('regEmail').value.trim();
    const password = document.getElementById('regPassword').value;
    if (!email || !password) {
      errorEl.textContent = 'Email và mật khẩu là bắt buộc.';
      errorEl.style.display = 'block';
      return;
    }

    let newUser = { email, password, role };
    if (role === 'customer') {
      newUser = {
        ...newUser,
        name: document.getElementById('regName').value.trim(),
        dob: document.getElementById('regDob').value,
        address: document.getElementById('regAddress').value.trim(),
        phone: document.getElementById('regPhone').value.trim(),
        avatar: 'https://images.unsplash.com/photo-1544723795-3fb6469f5b39?auto=format&fit=crop&w=160&q=80'
      };
    } else if (role === 'shipper') {
      newUser = {
        ...newUser,
        name: document.getElementById('regName').value.trim(),
        dob: document.getElementById('regDob').value,
        phone: document.getElementById('regPhone').value.trim(),
        vehicle: document.getElementById('regVehicle').value.trim(),
        totalOrders: 0,
        avatar: 'https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&w=160&q=80'
      };
    } else {
      newUser = {
        ...newUser,
        storeName: document.getElementById('regStoreName').value.trim(),
        address: document.getElementById('regAddress').value.trim(),
        phone: document.getElementById('regPhone').value.trim(),
        logo: 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=640&q=80'
      };
    }

    if (Object.values(newUser).some(value => value === '')) {
      errorEl.textContent = 'Vui lòng điền đầy đủ thông tin.';
      errorEl.style.display = 'block';
      return;
    }
    const users = getUsers(role);
    if (users.find(u => u.email === email)) {
      errorEl.textContent = 'Email đã tồn tại.';
      errorEl.style.display = 'block';
      return;
    }
    users.push(newUser);
    saveUsers(role, users);
    successEl.textContent = 'Đăng ký thành công! Chuyển sang đăng nhập...';
    successEl.style.display = 'block';
    document.getElementById('loginRole').value = role;
    document.getElementById('loginEmail').value = email;
    updateSubtitle();
    setTimeout(() => switchTab('login'), 800);
  });

  document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const role = document.getElementById('loginRole').value;
    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;
    const errorEl = document.getElementById('loginError');
    errorEl.style.display = 'none';
    const users = getUsers(role);
    const user = users.find(u => u.email === email && u.password === password);
    if (user) {
      localStorage.setItem('currentUser', JSON.stringify({ ...user, role }));
      window.location.href = 'user_profile.html';
    } else {
      errorEl.textContent = 'Email hoặc mật khẩu sai!';
      errorEl.style.display = 'block';
    }
  });