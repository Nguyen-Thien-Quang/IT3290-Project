(function() {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));
  if (!currentUser) { window.location.href = 'login.html'; return; }

  const API_BASE = 'api';
  const DEFAULT_IMG = 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=640&q=80';

  let profileData = null;
  const appContainer = document.getElementById('appContainer');

  async function fetchAPI(endpoint, options = {}) {
    // Ensure endpoint starts with /
    const path = endpoint.startsWith('/') ? endpoint : '/' + endpoint;
    try {
      const response = await fetch(API_BASE + path, {
        ...options,
        headers: {
          'Content-Type': 'application/json',
          ...options.headers
        }
      });
      if (response.status === 401) {
        window.location.href = 'login.html';
        return null;
      }
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      return { success: false, message: 'Lỗi kết nối server' };
    }
  }

  function formatMoney(value) { return Number(value || 0).toLocaleString('vi-VN') + 'đ'; }
  function todayISO() { return new Date().toISOString().split('T')[0]; }
  function firstDayISO() {
    const d = new Date();
    return new Date(d.getFullYear(), d.getMonth(), 1).toISOString().split('T')[0];
  }

  async function loadProfile() {
    let endpoint = '';
    if (currentUser.role === 'customer') endpoint = '/customer/profile';
    else if (currentUser.role === 'shipper') endpoint = '/shipper/profile';
    else endpoint = '/shop/profile';

    const data = await fetchAPI(endpoint);
    if (data && !data.error) {
      profileData = data;
      initUI();
    } else {
      window.location.href = 'login.html';
    }
  }

  function initUI() {
    if (currentUser.role === 'store') renderStoreUI();
    else if (currentUser.role === 'shipper') renderShipperUI();
    else renderCustomerUI();
  }

  function modal(title, bodyHtml) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalBody').innerHTML = bodyHtml;
    document.getElementById('modalBackdrop').classList.add('active');
  }
  document.getElementById('modalClose').addEventListener('click', () => document.getElementById('modalBackdrop').classList.remove('active'));
  document.getElementById('modalBackdrop').addEventListener('click', e => {
    if (e.target.id === 'modalBackdrop') document.getElementById('modalBackdrop').classList.remove('active');
  });

  function shell(roleTitle, navHtml, bodyHtml, title) {
    const displayName = profileData.hoTen || profileData.tenCuaHang || currentUser.email;
    const avatar = DEFAULT_IMG; // Backend currently doesn't provide avatar URL in profile
    appContainer.innerHTML = `
    <aside class="sidebar">
      <div class="logo-area"><i class="fas fa-utensils"></i> ${roleTitle}</div>
      <nav class="nav-menu">${navHtml}</nav>
      <div class="sidebar-footer">
        <span>${displayName}</span>
        <button class="btn btn-outline btn-sm" onclick="logout()">Đăng xuất</button>
      </div>
    </aside>
    <div class="main-wrapper">
      <header class="header">
        <span class="page-title" id="headerTitle">${title}</span>
        <div class="header-right"><img src="${avatar}" class="header-avatar" alt=""><span class="header-name">${displayName}</span></div>
      </header>
      <div class="content">${bodyHtml}</div>
    </div>`;
  }

  function bindNavigation(titleMap) {
    document.querySelectorAll('.nav-item').forEach(item => item.addEventListener('click', function() {
      const page = this.dataset.page;
      document.querySelectorAll('.page-section').forEach(section => section.classList.remove('active'));
      document.getElementById(page + 'Page').classList.add('active');
      document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
      this.classList.add('active');
      document.getElementById('headerTitle').textContent = titleMap[page] || page;
      if (window.pageHooks && window.pageHooks[page]) window.pageHooks[page]();
    }));
  }

  // --- CUSTOMER UI ---
  function renderCustomerUI() {
    shell('Khách hàng',
      `<button class="nav-item active" data-page="home"><i class="fas fa-home"></i> Trang chủ</button>
       <button class="nav-item" data-page="search"><i class="fas fa-search"></i> Tìm kiếm</button>
       <button class="nav-item" data-page="cart"><i class="fas fa-shopping-cart"></i> Giỏ hàng <span id="cartBadge" style="background:var(--primary);color:white;border-radius:50%;padding:2px 6px;font-size:11px;display:none;">0</span></button>
       <button class="nav-item" data-page="orders"><i class="fas fa-history"></i> Lịch sử đơn</button>
       <button class="nav-item" data-page="profile"><i class="fas fa-id-card"></i> Thông tin</button>`,
      `<div class="page-section active" id="homePage"></div>
       <div class="page-section" id="searchPage">
         <div class="card">
           <div class="section-title"><i class="fas fa-search"></i> Tìm món / Tìm cửa hàng</div>
           <div class="search-tabs"><button class="search-tab active" data-search-tab="food">Món ăn</button><button class="search-tab" data-search-tab="store">Cửa hàng</button></div>
           <div class="toolbar">
             <div class="form-group"><label>Từ khóa</label><input class="form-control" id="searchKeyword" placeholder="Nhập tên món hoặc cửa hàng"></div>
             <button class="btn btn-primary" id="searchBtn"><i class="fas fa-search"></i> Tìm</button>
           </div>
           <div class="menu-grid" id="searchResults"></div>
         </div>
       </div>
       <div class="page-section" id="cartPage">
         <div class="card">
           <div class="section-title"><i class="fas fa-shopping-cart"></i> Danh sách món đã thêm</div>
           <div id="cartItems"></div>
           <div class="form-group" style="margin-top:16px;"><label>Voucher ID (Tùy chọn)</label><input type="number" class="form-control" id="voucherIdInput"></div>
           <div class="form-group"><label>Phương thức thanh toán</label><select class="form-control" id="paymentMethodSelect"><option value="Tiền mặt">Tiền mặt</option><option value="Chuyển khoản">Chuyển khoản</option><option value="Ví điện tử">Ví điện tử</option></select></div>
           <div class="cart-summary">
             <div class="summary-line"><strong>Tổng</strong><span class="cart-total" id="cartTotal">0đ</span></div>
           </div>
           <button class="btn btn-primary btn-block" id="placeOrderBtn" style="margin-top:12px;"><i class="fas fa-check"></i> Đặt hàng</button>
         </div>
       </div>
       <div class="page-section" id="ordersPage"><div class="card"><div class="section-title"><i class="fas fa-history"></i> Danh sách đơn đã đặt</div><div class="orders-grid" id="customerOrdersList"></div></div></div>
       <div class="page-section" id="profilePage"><div class="card"><div class="section-title"><i class="fas fa-id-card"></i> Thông tin cá nhân</div><table class="info-table">
         <tr><td>Avatar</td><td><img class="sidebar-avatar" src="${DEFAULT_IMG}" alt=""></td></tr>
         <tr><td>Họ tên</td><td>${profileData.hoTen || ''}</td></tr><tr><td>Email</td><td>${currentUser.email}</td></tr><tr><td>SĐT</td><td>${profileData.sdt || ''}</td></tr><tr><td>Địa chỉ</td><td>${profileData.diaChi || ''}</td></tr>
       </table></div></div>`,
      'Trang chủ');

    let searchTab = 'food';
    window.pageHooks = { home: renderCustomerHome, cart: renderCart, orders: renderCustomerOrders, search: renderSearch };
    bindNavigation({ home:'Trang chủ', search:'Tìm món / Tìm cửa hàng', cart:'Giỏ hàng', orders:'Lịch sử đơn', profile:'Thông tin cá nhân' });

    document.querySelectorAll('.search-tab').forEach(tab => tab.addEventListener('click', function() {
      searchTab = this.dataset.searchTab;
      document.querySelectorAll('.search-tab').forEach(t => t.classList.remove('active'));
      this.classList.add('active');
      renderSearch();
    }));
    document.getElementById('searchBtn').addEventListener('click', renderSearch);
    document.getElementById('searchKeyword').addEventListener('input', renderSearch);
    document.getElementById('placeOrderBtn').addEventListener('click', placeOrder);

    async function renderSearch() {
      const keyword = document.getElementById('searchKeyword').value.trim();
      const results = document.getElementById('searchResults');
      
      if (searchTab === 'store') {
        const res = await fetchAPI('/shop/search?keyword=' + encodeURIComponent(keyword));
        if (res && res.success) {
          results.innerHTML = res.data.length ? res.data.map(store => `
          <div class="store-card">
            <img class="store-img" src="${DEFAULT_IMG}" alt="">
            <div class="store-name">${store.tenCuaHang}</div>
            <div class="muted">${store.diaChi || ''}</div>
            <button class="btn btn-outline btn-sm view-store" data-id="${store.idCuaHang}">Xem món</button>
          </div>`).join('') : '<div class="empty-message"><i class="fas fa-store"></i> Không tìm thấy cửa hàng</div>';
        }
      } else {
        const res = await fetchAPI('/food/search?keyword=' + encodeURIComponent(keyword));
        const foods = (res && res.success) ? res.data : [];
        results.innerHTML = foods.length ? foods.map(food => `
        <div class="menu-item-card">
          <img class="food-img" src="${food.img || DEFAULT_IMG}" alt="">
          <div class="food-name">${food.tenMon}</div>
          <div class="food-desc">Giá: ${formatMoney(food.gia)}</div>
          <button class="btn btn-primary btn-sm add-cart" data-id="${food.idMonAn}"><i class="fas fa-plus"></i> Thêm vào giỏ</button>
        </div>`).join('') : '<div class="empty-message"><i class="fas fa-utensils"></i> Không tìm thấy món ăn</div>';
      }
    }

    document.addEventListener('click', async (e) => {
      const addBtn = e.target.closest('.add-cart');
      const storeBtn = e.target.closest('.view-store');
      if (addBtn) {
        const res = await fetchAPI('/cart', {
          method: 'POST',
          body: JSON.stringify({ monAnId: Number(addBtn.dataset.id), quantity: 1 })
        });
        if (res && res.success) {
          alert('Đã thêm vào giỏ hàng');
          updateCartBadge();
        }
      }
      if (storeBtn) {
        const shopId = storeBtn.dataset.id;
        const res = await fetchAPI('/shop/foods?id=' + shopId);
        if (Array.isArray(res)) {
          modal('Menu cửa hàng', `<div class="menu-grid">${res.map(food => `
            <div class="menu-item-card">
              <div class="food-name">${food.tenMon}</div>
              <div class="food-price">${formatMoney(food.gia)}</div>
              <button class="btn btn-primary btn-sm add-cart" data-id="${food.idMonAn}">Thêm</button>
            </div>`).join('')}</div>`);
        }
      }
    });

    async function updateCartBadge() {
      const res = await fetchAPI('/cart');
      if (res && res.success && res.items) {
        const count = res.items.reduce((sum, i) => sum + i.soLuong, 0);
        const badge = document.getElementById('cartBadge');
        if (badge) {
          badge.textContent = count;
          badge.style.display = count > 0 ? 'inline' : 'none';
        }
      }
    }

    async function renderCart() {
      const res = await fetchAPI('/cart');
      const container = document.getElementById('cartItems');
      if (res && res.success && res.items && res.items.length > 0) {
        container.innerHTML = res.items.map(item => `
          <div class="cart-item">
            <img src="${item.img || DEFAULT_IMG}" alt="${item.tenMon}" style="width:60px;height:60px;object-fit:cover;border-radius:8px;">
            <div style="flex:1;margin-left:15px;">
              <div style="font-weight:600;font-size:15px;color:#333;">${item.tenMon}</div>
              <div style="color:#B8860B;font-weight:500;margin-top:4px;">${formatMoney(item.gia)}</div>
            </div>
            <div style="display:flex;align-items:center;gap:10px;">
              <input class="qty-input update-qty" type="number" min="1" value="${item.soLuong}" data-id="${item.idMonAn}" style="width:50px;text-align:center;">
              <button class="btn btn-danger btn-sm remove-cart" data-id="${item.idMonAn}" style="padding:5px 8px;"><i class="fas fa-trash"></i></button>
            </div>
          </div>`).join('');
        document.getElementById('cartTotal').textContent = formatMoney(res.cart ? res.cart.tongTien : 0);
      } else {
        container.innerHTML = '<div class="empty-message"><i class="fas fa-shopping-cart"></i> Giỏ hàng của bạn đang trống</div>';
        document.getElementById('cartTotal').textContent = '0đ';
      }
    }

    document.addEventListener('change', async (e) => {
      if (e.target.classList.contains('update-qty')) {
        const id = Number(e.target.dataset.id);
        const qty = Number(e.target.value);
        await fetchAPI('/cart', {
          method: 'PUT',
          body: JSON.stringify({ monAnId: id, quantity: qty })
        });
        renderCart();
        updateCartBadge();
      }
    });

    document.addEventListener('click', async (e) => {
      const rmBtn = e.target.closest('.remove-cart');
      if (rmBtn) {
        await fetchAPI('/cart?monAnId=' + rmBtn.dataset.id, { method: 'DELETE' });
        renderCart();
        updateCartBadge();
      }
    });

    async function placeOrder() {
      const phuongThuc = document.getElementById('paymentMethodSelect').value;
      const voucherId = document.getElementById('voucherIdInput').value || null;
      const res = await fetchAPI('/cart?action=order', {
        method: 'POST',
        body: JSON.stringify({ phuongThuc, voucherId: voucherId ? Number(voucherId) : null })
      });
      if (res && res.success) {
        alert('Đặt hàng thành công!');
        updateCartBadge();
        document.querySelector('[data-page="orders"]').click();
      } else {
        alert(res.message || 'Đặt hàng thất bại');
      }
    }

    async function renderCustomerHome() {
      const homeHtml = `
        <div class="card">
          <div class="section-title"><i class="fas fa-home"></i> Tổng quan</div>
          <div class="metrics-grid" id="homeMetrics">
            <div class="metric-card"><div class="metric-label">Giỏ hàng</div><div class="metric-value" id="homeCartCount">0</div></div>
            <div class="metric-card"><div class="metric-label">Đơn chờ</div><div class="metric-value" id="homePendingCount">0</div></div>
            <div class="metric-card"><div class="metric-label">Đã đặt</div><div class="metric-value" id="homeTotalOrders">0</div></div>
          </div>
        </div>
        <div class="card" id="homeRecentOrders">
          <div class="section-title"><i class="fas fa-clock"></i> Đơn gần đây</div>
          <div id="homeRecentList"><div class="empty-message"><i class="fas fa-inbox"></i> Chưa có đơn hàng nào</div></div>
        </div>
        <div class="card">
          <div class="section-title"><i class="fas fa-bolt"></i> Thao tác nhanh</div>
          <div style="display:flex;gap:12px;flex-wrap:wrap">
            <button class="btn btn-primary" onclick="document.querySelector('[data-page=search]')?.click()"><i class="fas fa-search"></i> Tìm món</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=cart]')?.click()"><i class="fas fa-shopping-cart"></i> Giỏ hàng</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=orders]')?.click()"><i class="fas fa-history"></i> Đơn hàng</button>
          </div>
        </div>`;
      document.getElementById('homePage').innerHTML = homeHtml;

      const cart = await fetchAPI('/cart');
      const orders = await fetchAPI('/order/history');
      if (cart && cart.success && cart.items) {
        const count = cart.items.reduce((s, i) => s + i.soLuong, 0);
        document.getElementById('homeCartCount').textContent = count;
      }
      if (orders && orders.success && orders.data) {
        const pending = orders.data.filter(o => o.trangThai === 'Chờ xác nhận' || o.trangThai === 'cho_xac_nhan').length;
        document.getElementById('homePendingCount').textContent = pending;
        document.getElementById('homeTotalOrders').textContent = orders.data.length;
        if (orders.data.length) {
          const recent = orders.data.slice(0, 3);
          document.getElementById('homeRecentList').innerHTML = recent.map(o => `
            <div class="order-card" style="margin-bottom:10px;">
              <div class="order-header"><span class="order-id">#${o.idDonHang}</span><span class="status-badge ${o.trangThai === 'Đã hủy' ? 'cancelled' : ''}">${o.trangThai}</span></div>
              <div style="display:flex;justify-content:space-between;align-items:center"><span class="muted">${o.thoiGianDat || ''}</span><span class="food-price">${formatMoney(o.tongTien)}</span></div>
            </div>`).join('');
        }
      }
    }

    async function renderCustomerOrders() {
      console.log('Rendering Customer Orders...');
      const res = await fetchAPI('/order/history');
      const container = document.getElementById('customerOrdersList');
      if (res && res.success) {
        console.log('Orders found:', res.data.length);
        container.innerHTML = res.data.length ? res.data.map(order => {
          const status = (order.trangThai || '').trim();
          const isPending = status === 'Chờ xác nhận' || status === 'cho_xac_nhan';
          return `
          <div class="order-card">
            <div class="order-header">
              <span class="order-id">#${order.idDonHang}</span>
              <div style="display: flex; gap: 8px; align-items: center;">
                <span class="status-badge ${status === 'Đã hủy' ? 'cancelled' : ''}">${status}</span>
                ${isPending ? `<button class="btn btn-danger btn-sm cancel-order-btn" data-id="${order.idDonHang}" style="padding: 4px 10px; font-weight: bold; font-size: 11px;">Hủy đơn</button>` : ''}
              </div>
            </div>
            <div class="muted">${order.thoiGianDat}</div>
            <div class="total">${formatMoney(order.tongTien)}</div>
            <div class="muted">Thanh toán: ${order.phuongThucThanhToan}</div>
          </div>`;
        }).join('') : '<div class="empty-message"><i class="fas fa-inbox"></i> Chưa có đơn hàng</div>';
      }
    }

    document.addEventListener('click', async (e) => {
      const cancelBtn = e.target.closest('.cancel-order-btn');
      if (cancelBtn) {
        console.log('Cancel button clicked for order:', cancelBtn.dataset.id);
        if (confirm('Bạn có chắc muốn hủy đơn hàng này?')) {
          const res = await fetchAPI('/order/customer?id=' + cancelBtn.dataset.id, { method: 'POST' });
          if (res && res.success) {
            alert('Đã hủy đơn hàng thành công');
            renderCustomerOrders();
          } else {
            alert(res.message || 'Không thể hủy đơn hàng');
          }
        }
      }
    });

    updateCartBadge();
    renderCustomerHome();
  }

  // --- STORE UI ---
  function renderStoreUI() {
    shell('Cửa hàng',
      `<button class="nav-item active" data-page="home"><i class="fas fa-home"></i> Trang chủ</button>
       <button class="nav-item" data-page="info"><i class="fas fa-info-circle"></i> Thông tin</button>
       <button class="nav-item" data-page="revenue"><i class="fas fa-chart-line"></i> Doanh thu</button>
       <button class="nav-item" data-page="menu"><i class="fas fa-utensils"></i> Menu</button>
       <button class="nav-item" data-page="orders"><i class="fas fa-receipt"></i> Đơn hàng</button>`,
      `<div class="page-section active" id="homePage"></div>
       <div class="page-section" id="infoPage"><div class="card"><div class="section-title">Thông tin cửa hàng</div><table class="info-table">
         <tr><td>Tên cửa hàng</td><td>${profileData.tenCuaHang || ''}</td></tr><tr><td>Email</td><td>${currentUser.email}</td></tr><tr><td>SĐT</td><td>${profileData.sdt || ''}</td></tr><tr><td>Địa chỉ</td><td>${profileData.diaChi || ''}</td></tr>
       </table></div></div>
       <div class="page-section" id="revenuePage">
         <div class="card">
           <div class="section-title">Phân tích kinh doanh</div>
           <div class="order-tabs">
             <button class="order-tab active" data-revenue-tab="stats">Doanh thu</button>
             <button class="order-tab" data-revenue-tab="bestseller">Món bán chạy</button>
           </div>
           
           <div id="revenueStatsView">
             <div class="toolbar">
               <div class="form-group"><label>Từ ngày</label><input class="form-control" type="date" id="fromDate" value="${firstDayISO()}"></div>
               <div class="form-group"><label>Đến ngày</label><input class="form-control" type="date" id="toDate" value="${todayISO()}"></div>
               <button class="btn btn-primary" id="filterRevenue">Lọc</button>
             </div>
             <div class="metrics-grid" id="revenueMetrics">
               <div class="metric-card"><div class="metric-label">Tổng doanh thu</div><div class="metric-value" id="metricRevenue">0đ</div></div>
               <div class="metric-card"><div class="metric-label">Số đơn hàng</div><div class="metric-value" id="metricOrderCount">0</div></div>
               <div class="metric-card"><div class="metric-label">TB mỗi đơn</div><div class="metric-value" id="metricAvgOrder">0đ</div></div>
             </div>
             <div class="orders-grid" id="revenueOrders"></div>
           </div>

           <div id="bestSellerView" style="display:none;">
             <div class="toolbar">
               <div class="form-group"><label>Số lượng món (K)</label><input class="form-control" type="number" id="bestSellerK" value="5" min="1" max="20"></div>
               <button class="btn btn-primary" id="refreshBestSeller">Xem top</button>
             </div>
             <div class="orders-grid" id="bestSellerList"></div>
           </div>
         </div>
       </div>
       <div class="page-section" id="menuPage"><div class="card"><div class="section-title">Menu món ăn</div><div class="menu-grid" id="storeMenuGrid"></div></div></div>
       <div class="page-section" id="ordersPage"><div class="card"><div class="section-title">Đơn hàng của quán</div><div class="orders-grid" id="storeOrders"></div></div></div>`,
      'Trang chủ');

    window.pageHooks = { home: renderStoreHome, revenue: renderRevenue, menu: renderStoreMenu, orders: renderStoreOrders };
    bindNavigation({ home:'Trang chủ', info:'Thông tin cửa hàng', revenue:'Phân tích kinh doanh', menu:'Menu', orders:'Đơn hàng' });
    document.getElementById('filterRevenue').addEventListener('click', renderRevenue);
    document.getElementById('refreshBestSeller').addEventListener('click', renderBestSellers);

    // Tab switching logic for Revenue page
    document.addEventListener('click', e => {
      const tab = e.target.closest('[data-revenue-tab]');
      if (tab) {
        const target = tab.dataset.revenueTab;
        document.querySelectorAll('[data-revenue-tab]').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        
        if (target === 'stats') {
          document.getElementById('revenueStatsView').style.display = 'block';
          document.getElementById('bestSellerView').style.display = 'none';
          renderRevenue();
        } else {
          document.getElementById('revenueStatsView').style.display = 'none';
          document.getElementById('bestSellerView').style.display = 'block';
          renderBestSellers();
        }
      }
    });

    async function renderBestSellers() {
      const k = document.getElementById('bestSellerK').value || 5;
      const res = await fetchAPI('/shop/food/bestSeller?k=' + k);
      const container = document.getElementById('bestSellerList');
      if (Array.isArray(res)) {
        container.innerHTML = res.length ? res.map((item, index) => `
          <div class="order-card">
            <div class="order-header">
              <span class="order-id">Top ${index + 1}</span>
              <span class="status-badge done">${formatMoney(item.totalRevenue)}</span>
            </div>
            <div style="font-weight: bold; font-size: 16px; margin: 8px 0;">${item.tenMon}</div>
            <div class="muted">ID Món: ${item.idMonAn}</div>
          </div>
        `).join('') : '<div class="empty-message"><i class="fas fa-chart-line"></i> Chưa có dữ liệu bán hàng</div>';
      }
    }

    async function renderRevenue() {
      const from = document.getElementById('fromDate').value;
      const to = document.getElementById('toDate').value;
      const res = await fetchAPI(`/shop/sales?startDate=${from}&endDate=${to}`);
      if (res) {
        document.getElementById('metricRevenue').textContent = formatMoney(res.totalRevenue || 0);
        const orders = res.orders || [];
        document.getElementById('metricOrderCount').textContent = orders.length;
        const avg = orders.length ? res.totalRevenue / orders.length : 0;
        document.getElementById('metricAvgOrder').textContent = formatMoney(avg);
        document.getElementById('revenueOrders').innerHTML = orders.length ? orders.map(order => `
          <div class="order-card"><div class="order-header"><span>#${order.idDonHang}</span><span>${order.trangThai}</span></div><div class="total">${formatMoney(order.tongTien)}</div></div>
        `).join('') : '<div class="empty-message"><i class="fas fa-receipt"></i> Không có dữ liệu</div>';
      }
    }

    let storeFoods = [];
    async function renderStoreMenu() {
      const res = await fetchAPI('/shop/foods?id=' + profileData.idCuaHang);
      if (Array.isArray(res)) {
        storeFoods = res;
        document.getElementById('storeMenuGrid').innerHTML = `
          <div class="menu-item-card" id="addFoodBtn" style="border: 2px dashed #B8860B; cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 200px;">
            <i class="fas fa-plus-circle" style="font-size: 40px; color: #B8860B;"></i>
            <span style="margin-top: 10px; font-weight: bold; color: #B8860B;">Thêm món mới</span>
          </div>
        ` + res.map(food => `
        <div class="menu-item-card">
          <img class="food-img" src="${food.img || DEFAULT_IMG}" alt="">
          <div class="food-name">${food.tenMon}</div>
          <div class="food-price">${formatMoney(food.gia)}</div>
          <div class="muted">Trạng thái: ${food.trangThai}</div>
          <div style="margin-top: 10px; display: flex; gap: 8px;">
            <button class="btn btn-outline btn-sm edit-food-btn" data-id="${food.idMonAn}">Sửa</button>
            <button class="btn btn-danger btn-sm delete-food-btn" data-id="${food.idMonAn}">Xóa</button>
          </div>
        </div>`).join('');
      }
    }

    document.addEventListener('click', async (e) => {
      if (currentUser.role !== 'store') return;

      const addBtn = e.target.closest('#addFoodBtn');
      const editBtn = e.target.closest('.edit-food-btn');
      const deleteBtn = e.target.closest('.delete-food-btn');

      if (addBtn) {
        modal('Thêm món mới', `
          <div class="form-group"><label>Tên món</label><input class="form-control" id="mTenMon"></div>
          <div class="form-group"><label>ID Loại</label><input class="form-control" type="number" id="mIdLoai" value="1"></div>
          <div class="form-group"><label>Giá</label><input class="form-control" type="number" id="mGia"></div>
          <div class="form-group"><label>Ảnh (URL)</label><input class="form-control" id="mImg"></div>
          <button class="btn btn-primary btn-block" id="saveAddFood" style="margin-top:10px;">Lưu</button>
        `);
        document.getElementById('saveAddFood').onclick = async () => {
          const payload = {
            tenMon: document.getElementById('mTenMon').value,
            idLoai: Number(document.getElementById('mIdLoai').value),
            gia: Number(document.getElementById('mGia').value),
            img: document.getElementById('mImg').value
          };
          const res = await fetchAPI('/food', { method: 'POST', body: JSON.stringify(payload) });
          if (res.success) { alert('Đã thêm!'); document.getElementById('modalClose').click(); renderStoreMenu(); }
          else alert(res.message);
        };
      }

      if (editBtn) {
        const id = Number(editBtn.dataset.id);
        const food = storeFoods.find(f => f.idMonAn === id);
        if (food) {
          modal('Sửa món ăn', `
            <div class="form-group"><label>Tên món</label><input class="form-control" id="mTenMon" value="${food.tenMon}"></div>
            <div class="form-group"><label>Giá</label><input class="form-control" type="number" id="mGia" value="${food.gia}"></div>
            <div class="form-group"><label>Loại Món Ăn (ID)</label><input class="form-control" type="number" id="mIdLoai" value="${food.idLoai}"></div>
            <div class="form-group"><label>Trạng thái</label>
              <select class="form-control" id="mTrangThai">
                <option value="Còn hàng" ${food.trangThai === 'Còn hàng' ? 'selected' : ''}>Còn hàng</option>
                <option value="Hết hàng" ${food.trangThai === 'Hết hàng' ? 'selected' : ''}>Hết hàng</option>
              </select>
            </div>
            <button class="btn btn-primary btn-block" id="saveEditFood" style="margin-top:10px;">Cập nhật</button>
          `);
          document.getElementById('saveEditFood').onclick = async () => {
            const payload = {
              idMonAn: id,
              tenMon: document.getElementById('mTenMon').value,
              gia: Number(document.getElementById('mGia').value),
              trangThai: document.getElementById('mTrangThai').value,
              idLoai: Number(document.getElementById('mIdLoai').value),
              img: food.img // Keep existing image
            };
            const res = await fetchAPI('/food', { method: 'PUT', body: JSON.stringify(payload) });
            if (res.success) { alert('Đã cập nhật!'); document.getElementById('modalClose').click(); renderStoreMenu(); }
            else alert(res.message);
          };
        }
      }

      if (deleteBtn) {
        if (confirm('Bạn có chắc muốn xóa món này?')) {
          const res = await fetchAPI('/food?id=' + deleteBtn.dataset.id, { method: 'DELETE' });
          if (res.success) { alert('Đã xóa!'); renderStoreMenu(); }
          else alert(res.message);
        }
      }
    });

    async function renderStoreOrders() {
      const res = await fetchAPI('/order/history');
      if (res && res.success) {
        document.getElementById('storeOrders').innerHTML = res.data.length ? res.data.map(order => `
          <div class="order-card"><div class="order-header"><span>#${order.idDonHang}</span><span>${order.trangThai}</span></div><div class="total">${formatMoney(order.tongTien)}</div><small>${order.thoiGianDat}</small></div>
        `).join('') : '<div class="empty-message"><i class="fas fa-inbox"></i> Chưa có đơn hàng</div>';
      }
    }

    async function renderStoreHome() {
      const homeHtml = `
        <div class="card">
          <div class="section-title"><i class="fas fa-home"></i> Tổng quan</div>
          <div class="metrics-grid" id="storeHomeMetrics">
            <div class="metric-card"><div class="metric-label">Menu</div><div class="metric-value" id="homeMenuCount">0</div></div>
            <div class="metric-card"><div class="metric-label">Đơn chờ</div><div class="metric-value" id="homeStorePending">0</div></div>
          </div>
        </div>
        <div class="card">
          <div class="section-title"><i class="fas fa-bolt"></i> Thao tác nhanh</div>
          <div style="display:flex;gap:12px;flex-wrap:wrap">
            <button class="btn btn-primary" onclick="document.querySelector('[data-page=menu]')?.click()"><i class="fas fa-utensils"></i> Quản lý menu</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=revenue]')?.click()"><i class="fas fa-chart-line"></i> Doanh thu</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=orders]')?.click()"><i class="fas fa-receipt"></i> Đơn hàng</button>
          </div>
        </div>`;
      document.getElementById('homePage').innerHTML = homeHtml;

      const foods = await fetchAPI('/shop/foods?id=' + profileData.idCuaHang);
      if (Array.isArray(foods)) {
        document.getElementById('homeMenuCount').textContent = foods.length;
      }

      const orders = await fetchAPI('/order/history');
      if (orders && orders.success && orders.data) {
        const pending = orders.data.filter(o => o.trangThai === 'Chờ xác nhận' || o.trangThai === 'cho_xac_nhan').length;
        document.getElementById('homeStorePending').textContent = pending;
      }
    }

    renderStoreHome();
  }

  // --- SHIPPER UI ---
  function renderShipperUI() {
    shell('Shipper',
      `<button class="nav-item active" data-page="home"><i class="fas fa-home"></i> Trang chủ</button>
       <button class="nav-item" data-page="pending"><i class="fas fa-clipboard-list"></i> Đơn hiện tại</button>
       <button class="nav-item" data-page="accepted"><i class="fas fa-truck"></i> Đơn nhận giao</button>
       <button class="nav-item" data-page="history"><i class="fas fa-history"></i> Lịch sử đơn</button>
       <button class="nav-item" data-page="profile"><i class="fas fa-id-card"></i> Thông tin</button>`,
      `<div class="page-section active" id="homePage"></div>
       <div class="page-section" id="pendingPage"><div class="card"><div class="section-title">Đơn hàng đang chờ</div><div class="orders-grid" id="pendingOrdersGrid"></div></div></div>
       <div class="page-section" id="acceptedPage"><div class="card"><div class="section-title">Đơn hàng đang giao</div><div class="orders-grid" id="acceptedOrdersGrid"></div></div></div>
       <div class="page-section" id="historyPage"><div class="card"><div class="section-title">Lịch sử đơn hàng</div><div class="orders-grid" id="historyOrdersGrid"></div></div></div>
       <div class="page-section" id="profilePage"><div class="card"><div class="section-title">Thông tin cá nhân</div><table class="info-table">
         <tr><td>Họ tên</td><td>${profileData.hoTen || ''}</td></tr><tr><td>Email</td><td>${currentUser.email}</td></tr><tr><td>SĐT</td><td>${profileData.sdt || ''}</td></tr><tr><td>Ngày sinh</td><td>${profileData.ngaySinh || ''}</td></tr>
       </table></div></div>`,
      'Trang chủ');

    window.pageHooks = { home: renderShipperHome, pending: renderPending, accepted: renderAccepted, history: renderHistory };
    bindNavigation({ home:'Trang chủ', pending:'Đơn hiện tại', accepted:'Đơn nhận giao', history:'Lịch sử đơn hàng', profile:'Thông tin cá nhân' });

    async function renderShipperHome() {
      const homeHtml = `
        <div class="card">
          <div class="section-title"><i class="fas fa-home"></i> Tổng quan</div>
          <div class="metrics-grid" id="shipperHomeMetrics">
            <div class="metric-card"><div class="metric-label">Đơn chờ</div><div class="metric-value" id="homeShipperPending">0</div></div>
            <div class="metric-card"><div class="metric-label">Đang giao</div><div class="metric-value" id="homeShipperDelivering">0</div></div>
            <div class="metric-card"><div class="metric-label">Đã giao</div><div class="metric-value" id="homeShipperDone">0</div></div>
          </div>
        </div>
        <div class="card">
          <div class="section-title"><i class="fas fa-bolt"></i> Thao tác nhanh</div>
          <div style="display:flex;gap:12px;flex-wrap:wrap">
            <button class="btn btn-primary" onclick="document.querySelector('[data-page=pending]')?.click()"><i class="fas fa-clipboard-list"></i> Xem đơn chờ</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=accepted]')?.click()"><i class="fas fa-truck"></i> Đơn đang giao</button>
            <button class="btn btn-outline" onclick="document.querySelector('[data-page=history]')?.click()"><i class="fas fa-history"></i> Lịch sử</button>
          </div>
        </div>`;
      document.getElementById('homePage').innerHTML = homeHtml;

      const pending = await fetchAPI('/order');
      if (pending && pending.success) {
        document.getElementById('homeShipperPending').textContent = pending.data ? pending.data.length : 0;
      }
      const delivering = await fetchAPI('/order/shipping');
      if (delivering && delivering.success) {
        document.getElementById('homeShipperDelivering').textContent = delivering.data ? delivering.data.length : 0;
      }
      const history = await fetchAPI('/order/history');
      if (history && history.success && history.data) {
        const done = history.data.filter(o => o.trangThai === 'Hoàn thành' || o.trangThai === 'done').length;
        document.getElementById('homeShipperDone').textContent = done;
      }
    }

    async function renderPending() {
      const res = await fetchAPI('/order');
      if (res && res.success) {
        document.getElementById('pendingOrdersGrid').innerHTML = res.data.length ? res.data.map(order => `
          <div class="order-card">
            <div class="order-header"><span>#${order.idDonHang}</span><span class="status-badge">${order.trangThai}</span></div>
            <div class="total">${formatMoney(order.tongTien)}</div>
            <button class="btn btn-primary accept-btn" data-id="${order.idDonHang}">Nhận giao</button>
          </div>        `).join('') : '<div class="empty-message"><i class="fas fa-clipboard-list"></i> Không có đơn chờ</div>';
      }
    }

    async function renderAccepted() {
      const res = await fetchAPI('/order/shipping');
      if (res && res.success) {
        document.getElementById('acceptedOrdersGrid').innerHTML = res.data.length ? res.data.map(order => `
          <div class="order-card">
            <div class="order-header"><span>#${order.idDonHang}</span><span class="status-badge shipping">${order.trangThai}</span></div>
            <div class="total">${formatMoney(order.tongTien)}</div>
            <button class="btn btn-primary done-btn" data-id="${order.idDonHang}">Hoàn thành</button>
          </div>        `).join('') : '<div class="empty-message"><i class="fas fa-truck"></i> Chưa có đơn đang giao</div>';
      }
    }

    async function renderHistory() {
      const res = await fetchAPI('/order/history');
      if (res && res.success) {
        document.getElementById('historyOrdersGrid').innerHTML = res.data.length ? res.data.map(order => `
          <div class="order-card">
            <div class="order-header"><span>#${order.idDonHang}</span><span class="status-badge done">${order.trangThai}</span></div>
            <div class="total">${formatMoney(order.tongTien)}</div>
            <div class="muted">${order.thoiGianDat}</div>
          </div>        `).join('') : '<div class="empty-message"><i class="fas fa-history"></i> Chưa có lịch sử</div>';
      }
    }

    document.addEventListener('click', async (e) => {
      const acceptBtn = e.target.closest('.accept-btn');
      const doneBtn = e.target.closest('.done-btn');
      if (acceptBtn) {
        const res = await fetchAPI('/order?id=' + acceptBtn.dataset.id, { method: 'POST' });
        if (res && res.success) { alert('Đã nhận đơn!'); renderPending(); }
      }
      if (doneBtn) {
        const res = await fetchAPI('/order/shipping?id=' + doneBtn.dataset.id, { method: 'POST' });
        if (res && res.success) { alert('Đã hoàn thành!'); renderAccepted(); }
      }
    });

    renderShipperHome();
  }

  window.logout = function() {
    localStorage.removeItem('currentUser');
    window.location.href = 'login.html';
  };

  loadProfile();
})();