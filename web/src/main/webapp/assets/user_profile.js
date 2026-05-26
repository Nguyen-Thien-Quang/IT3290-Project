const API_BASE = '/OnlineFoodWeb_war/';

document.getElementById('fetchBtn').addEventListener('click', async () => {
    const customerId = document.getElementById('customerId').value.trim();
    const resultDiv = document.getElementById('result');

    if (!customerId) {
        resultDiv.innerHTML = '<div class="error">Vui lòng nhập ID khách hàng.</div>';
        return;
    }

    resultDiv.innerHTML = '<div class="loading">Đang tải...</div>';

    try {
        const response = await fetch(`${API_BASE}user/profile/${customerId}`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Khách hàng không tồn tại.');
            } else if (response.status === 400) {
                throw new Error('ID không hợp lệ.');
            } else {
                throw new Error('Lỗi server.');
            }
        }

        const customer = await response.json();
        displayCustomerInfo(customer);
    } catch (error) {
        resultDiv.innerHTML = `<div class="error">${error.message}</div>`;
    }
});

function displayCustomerInfo(customer) {
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = `
        <div class="user-info">
            <h2><i class="fas fa-user-circle"></i> Thông tin khách hàng</h2>
            <div class="info-item">
                <span class="info-label">ID Khách hàng:</span> ${customer.idKhachHang}
            </div>
            <div class="info-item">
                <span class="info-label">ID Tài khoản:</span> ${customer.idTaiKhoan}
            </div>
            <div class="info-item">
                <span class="info-label">Họ và tên:</span> ${customer.hoTen}
            </div>
            <div class="info-item">
                <span class="info-label">Ngày sinh:</span> ${customer.ngaySinh ? new Date(customer.ngaySinh).toLocaleDateString('vi-VN') : 'Chưa cập nhật'}
            </div>
            <div class="info-item">
                <span class="info-label">Địa chỉ:</span> ${customer.diaChi || 'Chưa cập nhật'}
            </div>
            <div class="info-item">
                <span class="info-label">Số điện thoại:</span> ${customer.sdt || 'Chưa cập nhật'}
            </div>
        </div>
    `;
}