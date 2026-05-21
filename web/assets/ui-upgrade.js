// Modern UI Upgrade 2026 - theme switch + local AI chatbox
(function () {
  'use strict';

  var STORAGE_KEY = 'qltv-theme';

  function ready(fn) {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  }

  function getSavedTheme() {
    return localStorage.getItem(STORAGE_KEY) || localStorage.getItem('theme') || 'light';
  }

  function applyTheme(theme, silent) {
    var dark = theme === 'dark';
    document.documentElement.classList.toggle('dark', dark);
    document.documentElement.classList.toggle('light', !dark);
    document.body.classList.toggle('dark', dark);
    document.body.classList.toggle('light', !dark);
    localStorage.setItem(STORAGE_KEY, theme);
    localStorage.setItem('theme', theme);

    document.querySelectorAll('.theme-toggle').forEach(function (btn) {
      btn.innerHTML = dark
        ? '<i class="fa-solid fa-sun"></i><span class="theme-label">Sáng</span>'
        : '<i class="fa-solid fa-moon"></i><span class="theme-label">Tối</span>';
      btn.setAttribute('aria-label', dark ? 'Chuyển sang chế độ sáng' : 'Chuyển sang chế độ tối');
      btn.title = dark ? 'Chuyển sang chế độ sáng' : 'Chuyển sang chế độ tối';
    });

    if (!silent) showThemeToast(dark ? 'Đã bật chế độ tối' : 'Đã bật chế độ sáng');
  }

  function toggleTheme() {
    var isDark = document.documentElement.classList.contains('dark') || document.body.classList.contains('dark');
    applyTheme(isDark ? 'light' : 'dark');
  }

  function showThemeToast(message) {
    var toast = document.querySelector('.theme-toast');
    if (!toast) {
      toast = document.createElement('div');
      toast.className = 'theme-toast';
      document.body.appendChild(toast);
    }
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(toast._timer);
    toast._timer = setTimeout(function () { toast.classList.remove('show'); }, 1800);
  }

  function createThemeButton() {
    if (document.querySelector('.theme-toggle')) return;

    var btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'theme-toggle';
    btn.onclick = toggleTheme;

    var topbar = document.querySelector('.topbar');
    var userChip = document.querySelector('.user-chip');

    if (topbar) {
      if (userChip && userChip.parentNode === topbar) topbar.insertBefore(btn, userChip);
      else topbar.appendChild(btn);
    } else {
      btn.classList.add('theme-toggle-floating');
      document.body.appendChild(btn);
    }
  }

  function enhanceExistingUI() {
    document.querySelectorAll('.nav a').forEach(function (link) {
      link.addEventListener('click', function () {
        document.querySelectorAll('.nav a').forEach(function (a) { a.classList.remove('active'); });
        link.classList.add('active');
      });
    });

    document.querySelectorAll('table').forEach(function (table) {
      table.classList.add('table', 'table-hover', 'align-middle');
    });

    document.querySelectorAll('.panel, .stat-card, .modal-form').forEach(function (el, index) {
      el.style.animationDelay = Math.min(index * 45, 260) + 'ms';
    });

    document.querySelectorAll('.brand-panel').forEach(function (panel) {
      panel.setAttribute('aria-label', 'Ảnh minh họa thư viện số');
    });
  }

  function getCurrentRole() {
    var text = ((document.querySelector('.role-pill') || {}).textContent || '').toLowerCase();
    var title = (document.title || '').toLowerCase();
    var body = document.body ? document.body.textContent.toLowerCase().slice(0, 1200) : '';
    if (text.indexOf('admin') >= 0 || title.indexOf('admin') >= 0 || body.indexOf('quản trị') >= 0) return 'admin';
    if (text.indexOf('staff') >= 0 || text.indexOf('nhân viên') >= 0 || title.indexOf('staff') >= 0 || body.indexOf('nhân viên') >= 0) return 'staff';
    return 'customer';
  }

  function createChatbox() {
    if (document.querySelector('.ai-chat-widget')) return;
    var role = getCurrentRole();
    var roleName = role === 'admin' ? 'Admin' : role === 'staff' ? 'Staff' : 'Khách hàng';
    var subtitle = role === 'admin'
      ? 'Tư vấn quản trị, tài khoản, thống kê'
      : role === 'staff'
        ? 'Tư vấn duyệt mượn, giao/trả sách'
        : 'Tư vấn tìm kiếm, mượn sách, lịch sử';

    var widget = document.createElement('div');
    widget.className = 'ai-chat-widget ai-chat-' + role;
    widget.innerHTML =
      '<div class="ai-chat-panel" role="dialog" aria-label="Chat hỗ trợ thư viện">' +
        '<div class="ai-chat-header ' + role + '">' +
          '<div><strong>Trợ lý ' + roleName + '</strong><span>' + subtitle + '</span></div>' +
          '<button class="ai-chat-close" type="button" aria-label="Đóng">×</button>' +
        '</div>' +
        '<div class="ai-chat-messages"></div>' +
        '<div>' +
          '<div class="ai-suggestions"></div>' +
          '<form class="ai-chat-form">' +
            '<input type="text" placeholder="Nhập câu hỏi..." autocomplete="off" />' +
            '<button type="submit">Gửi</button>' +
          '</form>' +
        '</div>' +
      '</div>' +
      '<button class="ai-chat-button" type="button" aria-label="Mở chat hỗ trợ"><i class="fa-solid fa-comments"></i></button>';

    document.body.appendChild(widget);

    var messages = widget.querySelector('.ai-chat-messages');
    var form = widget.querySelector('.ai-chat-form');
    var input = widget.querySelector('.ai-chat-form input');
    var suggestions = widget.querySelector('.ai-suggestions');

    var packs = {
      admin: [
        ['Tài khoản', 'Admin quản lý tài khoản tại mục Quản lý người dùng: thêm Staff, khóa/mở Customer và kiểm tra quyền đăng nhập.'],
        ['Thống kê', 'Dashboard Admin nên xem tổng sách, số sách còn, sách đang mượn, quá hạn, tổng Customer/Staff và tiền phạt.'],
        ['Phân quyền', 'Admin có toàn quyền; Staff xử lý sách và mượn/trả; Customer chỉ xem sách, gửi yêu cầu và xem lịch sử cá nhân.']
      ],
      staff: [
        ['Duyệt mượn', 'Staff vào Yêu cầu mượn sách, kiểm tra Customer, sách còn số lượng và lịch sử quá hạn rồi chọn Duyệt hoặc Từ chối.'],
        ['Giao sách', 'Khi khách đến nhận, Staff bấm Xác nhận giao sách. Lúc này phiếu chuyển Borrowed và số lượng sách giảm 1.'],
        ['Trả sách', 'Khi nhận sách, Staff chọn tình trạng: bình thường, hư hỏng hoặc mất. Trả bình thường thì cộng lại số lượng sách.']
      ],
      customer: [
        ['Tìm sách', 'Nhập tên sách hoặc tác giả ở ô tìm kiếm. Có thể xem số lượng còn và trạng thái trước khi bấm Mượn.'],
        ['Mượn sách', 'Bấm nút Mượn ở dòng sách còn số lượng. Nếu hệ thống dùng quy trình duyệt, yêu cầu sẽ chờ Staff xác nhận.'],
        ['Lịch sử', 'Vào Sách đang mượn hoặc Lịch sử mượn để xem hạn trả, trạng thái chưa trả, quá hạn hoặc đã trả.']
      ]
    };

    packs[role].forEach(function (item) {
      var btn = document.createElement('button');
      btn.type = 'button';
      btn.textContent = item[0];
      btn.setAttribute('data-q', item[0]);
      suggestions.appendChild(btn);
    });

    function addMsg(text, type) {
      var msg = document.createElement('div');
      msg.className = 'ai-msg ' + type;
      msg.textContent = text;
      messages.appendChild(msg);
      messages.scrollTop = messages.scrollHeight;
    }

    function answer(question) {
      var q = (question || '').toLowerCase();
      var selected = packs[role].find(function (item) { return q.indexOf(item[0].toLowerCase()) >= 0; });
      if (selected) return selected[1];
      if (q.indexOf('tối') >= 0 || q.indexOf('dark') >= 0 || q.indexOf('sáng') >= 0) return 'Bấm nút Sáng/Tối trên topbar. Bản này đã sửa bảng, card, form và chữ trong dark mode để dễ đọc hơn.';
      if (q.indexOf('lỗi') >= 0 || q.indexOf('không') >= 0) return 'Bạn kiểm tra thông báo trên màn hình, quyền tài khoản, dữ liệu SQL và trạng thái phiếu/sách. Nếu gửi ảnh lỗi, có thể dò đúng JSP/Servlet cần sửa.';
      if (q.indexOf('mượn') >= 0) return role === 'customer' ? packs.customer[1][1] : packs.staff[0][1];
      if (q.indexOf('trả') >= 0) return packs.staff[2][1];
      if (q.indexOf('admin') >= 0 || q.indexOf('quyền') >= 0) return packs.admin[2][1];
      return role === 'admin'
        ? 'Mình hỗ trợ Admin về tài khoản, phân quyền, sách, phiếu mượn và thống kê.'
        : role === 'staff'
          ? 'Mình hỗ trợ Staff về duyệt yêu cầu, giao sách, trả sách, quá hạn và phiếu phạt.'
          : 'Mình hỗ trợ Customer về tìm sách, mượn sách, lịch sử mượn, đổi mật khẩu và thông tin cá nhân.';
    }

    widget.querySelector('.ai-chat-button').addEventListener('click', function () {
      widget.classList.toggle('open');
      if (widget.classList.contains('open') && messages.children.length === 0) {
        addMsg(role === 'admin'
          ? 'Xin chào Admin! Bạn muốn kiểm tra tài khoản, thống kê hay quản lý sách?'
          : role === 'staff'
            ? 'Xin chào Staff! Bạn muốn duyệt yêu cầu, giao sách hay xác nhận trả sách?'
            : 'Xin chào! Bạn muốn tìm sách, mượn sách hay xem lịch sử mượn?', 'bot');
      }
    });

    widget.querySelector('.ai-chat-close').addEventListener('click', function () { widget.classList.remove('open'); });
    widget.querySelectorAll('.ai-suggestions button').forEach(function (btn) {
      btn.addEventListener('click', function () {
        var q = btn.getAttribute('data-q'); addMsg(q, 'user'); setTimeout(function () { addMsg(answer(q), 'bot'); }, 180);
      });
    });
    form.addEventListener('submit', function (e) {
      e.preventDefault(); var q = input.value.trim(); if (!q) return; addMsg(q, 'user'); input.value = ''; setTimeout(function () { addMsg(answer(q), 'bot'); }, 220);
    });
  }

  // Apply saved theme as soon as possible when this file is loaded.
  try { applyTheme(getSavedTheme(), true); } catch (e) {}

  ready(function () {
    enhanceExistingUI();
    createThemeButton();
    applyTheme(getSavedTheme(), true);
    // Chatbot is handled by luxury-ui.js to avoid duplicated widgets.
  });

  window.toggleTheme = toggleTheme;
  window.applyLibraryTheme = applyTheme;
})();
