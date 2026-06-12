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
      <div class="form-group"><label>Số điện thoại</label><input type="text" id="regPhone" required></div>`;
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

document.getElementById('registerForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  const role = document.getElementById('registerRole').value;
  const errorEl = document.getElementById('registerError');
  const successEl = document.getElementById('registerSuccess');
  errorEl.style.display = 'none';
  successEl.style.display = 'none';

  const email = document.getElementById('regEmail').value.trim();
  const password = document.getElementById('regPassword').value;

  let payload = { email, password };
  let endpoint = '';

  if (role === 'customer') {
    endpoint = 'api/customer/register';
    payload.name = document.getElementById('regName').value.trim();
    payload.birthday = document.getElementById('regDob').value;
    payload.address = document.getElementById('regAddress').value.trim();
    payload.SDT = document.getElementById('regPhone').value.trim();
  } else if (role === 'shipper') {
    endpoint = 'api/shipper/register';
    payload.name = document.getElementById('regName').value.trim();
    payload.birthday = document.getElementById('regDob').value;
    payload.SDT = document.getElementById('regPhone').value.trim();
  } else {
    endpoint = 'api/shop/register';
    payload.name = document.getElementById('regStoreName').value.trim();
    payload.address = document.getElementById('regAddress').value.trim();
    payload.SDT = document.getElementById('regPhone').value.trim();
  }

  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    const result = await response.json();
    if (result.success) {
      successEl.textContent = result.message || 'Đăng ký thành công!';
      successEl.style.display = 'block';
      setTimeout(() => switchTab('login'), 1500);
    } else {
      errorEl.textContent = result.message || 'Đăng ký thất bại.';
      errorEl.style.display = 'block';
    }
  } catch (error) {
    console.error('Register Error:', error);
    errorEl.textContent = 'Lỗi kết nối server. Vui lòng kiểm tra console.';
    errorEl.style.display = 'block';
  }
});

document.getElementById('loginForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  const role = document.getElementById('loginRole').value;
  const email = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('loginPassword').value;
  const errorEl = document.getElementById('loginError');
  errorEl.style.display = 'none';

  let endpoint = '';
  if (role === 'customer') endpoint = 'api/customer/login';
  else if (role === 'shipper') endpoint = 'api/shipper/login';
  else endpoint = 'api/shop/login';

  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    const result = await response.json();
    if (result.success) {
      // Store current user info for frontend use (role is critical)
      localStorage.setItem('currentUser', JSON.stringify({
        email: result.email,
        role: role // 'customer', 'shipper', or 'store'
      }));
      window.location.href = 'user_profile.html';
    } else {
      errorEl.textContent = result.message || 'Email hoặc mật khẩu sai!';
      errorEl.style.display = 'block';
    }
  } catch (error) {
    console.error('Login Error:', error);
    errorEl.textContent = 'Lỗi kết nối server. Vui lòng kiểm tra console.';
    errorEl.style.display = 'block';
  }
});