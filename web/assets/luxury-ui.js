(function () {
  function ready(fn) {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  }

  ready(function () {
    initLuxuryEffects();
    initRoleBusinessGuide();
    enhanceBusinessTables();
    initSmartChatbot();
  });

  function initLuxuryEffects() {
    document.body.insertAdjacentHTML('afterbegin', '<div class="lux-spotlight"></div><div class="lux-orb o1"></div><div class="lux-orb o2"></div><div class="lux-orb o3"></div>');

    var spotlight = document.querySelector('.lux-spotlight');
    window.addEventListener('pointermove', function (e) {
      if (!spotlight) return;
      spotlight.style.setProperty('--mx', e.clientX + 'px');
      spotlight.style.setProperty('--my', e.clientY + 'px');
    }, { passive: true });

    var targets = document.querySelectorAll('.visual-hero,.stat-card,section[id],.card,.panel,.table-card,.form-panel,.brand-panel');
    targets.forEach(function (el) { el.classList.add('lux-reveal'); });

    if ('IntersectionObserver' in window) {
      var io = new IntersectionObserver(function (entries) {
        entries.forEach(function (en) {
          if (en.isIntersecting) {
            en.target.classList.add('is-visible');
            io.unobserve(en.target);
          }
        });
      }, { threshold: .08 });
      targets.forEach(function (el) { io.observe(el); });
    } else {
      targets.forEach(function (el) { el.classList.add('is-visible'); });
    }

    document.querySelectorAll('.stat-card,.book-card,.visual-hero,.form-panel').forEach(function (card) {
      card.addEventListener('pointermove', function (e) {
        var r = card.getBoundingClientRect();
        var x = (e.clientX - r.left) / r.width - .5;
        var y = (e.clientY - r.top) / r.height - .5;
        card.style.transform = 'perspective(900px) rotateX(' + (-y * 4) + 'deg) rotateY(' + (x * 4) + 'deg) translateY(-3px)';
      });
      card.addEventListener('pointerleave', function () { card.style.transform = ''; });
    });

    var fab = document.createElement('div');
    fab.className = 'lux-command';
    fab.innerHTML = '<button class="lux-fab" type="button" title="Lên đầu trang" aria-label="Lên đầu trang">↑</button>';
    document.body.appendChild(fab);
    fab.querySelector('button').addEventListener('click', function () {
      window.scrollTo({ top: 0, behavior: 'smooth' });
      toast('Đã cuộn lên đầu trang');
    });

    var navs = document.querySelectorAll('.nav a[href^="#"]');
    navs.forEach(function (a) {
      a.addEventListener('click', function () {
        navs.forEach(function (n) { n.classList.remove('active'); });
        a.classList.add('active');
      });
    });
  }

  function toast(msg) {
    var t = document.querySelector('.lux-toast');
    if (!t) {
      t = document.createElement('div');
      t.className = 'lux-toast';
      document.body.appendChild(t);
    }
    t.textContent = msg;
    t.classList.add('show');
    setTimeout(function () { t.classList.remove('show'); }, 2200);
  }

  function initRoleBusinessGuide() {
    var role = detectRole();
    if (!role || document.querySelector('.role-business-guide')) return;

    var hero = document.querySelector('.visual-hero');
    var main = document.querySelector('.main');
    if (!hero || !main) return;

    var guide = getBusinessGuide(role);
    if (!guide) return;

    hero.insertAdjacentHTML('afterend', buildBusinessGuideMarkup(guide));
  }

  function getBusinessGuide(role) {
    var guides = {
      customer: {
        label: 'Quy trình độc giả',
        title: 'Mượn sách rõ ràng từ lúc tìm đến lúc trả',
        subtitle: 'Các bước chính giúp bạn biết sách còn hay hết, yêu cầu đang ở trạng thái nào và có phát sinh phí phạt không.',
        steps: [
          ['01', 'Tìm sách', 'Nhập tên sách, tác giả hoặc thể loại để lọc nhanh danh sách phù hợp.'],
          ['02', 'Gửi yêu cầu mượn', 'Chỉ bấm Mượn khi sách còn. Hệ thống gửi yêu cầu để nhân viên duyệt.'],
          ['03', 'Theo dõi trạng thái', 'Xem Sách đang mượn và Lịch sử mượn để biết hạn trả, quá hạn hoặc phí phạt.']
        ],
        rules: ['Không mượn khi sách hết', 'Không gửi trùng một sách', 'Có sách quá hạn thì nên trả trước', 'Đổi mật khẩu định kỳ để bảo vệ tài khoản'],
        actions: [
          ['Tìm sách', '#mgmt-book'],
          ['Sách đang mượn', '#borrowed'],
          ['Lịch sử mượn', '#history']
        ]
      },
      staff: {
        label: 'Quy trình nhân viên',
        title: 'Xử lý mượn trả theo đúng nghiệp vụ thư viện',
        subtitle: 'Tập trung vào duyệt yêu cầu, giao sách, xác nhận trả, phát hiện quá hạn và tạo phiếu phạt khi cần.',
        steps: [
          ['01', 'Duyệt yêu cầu', 'Kiểm tra độc giả, sách, số lượng còn và lịch sử quá hạn trước khi duyệt.'],
          ['02', 'Giao và trả sách', 'Khi khách nhận sách thì xác nhận giao. Khi khách trả sách thì kiểm tra tình trạng thực tế.'],
          ['03', 'Xử lý phạt', 'Quá hạn, hư hỏng hoặc mất sách cần được ghi nhận rõ ràng để tạo phiếu phạt.']
        ],
        rules: ['Chỉ trừ số lượng khi giao sách', 'Trả bình thường thì cộng lại sách', 'Sách mất không cộng lại số lượng', 'Không bỏ trống phiếu quá hạn'],
        actions: [
          ['Duyệt yêu cầu', '#pending-borrow-section'],
          ['Xác nhận trả', '#return-confirm-section'],
          ['Quá hạn', '#overdue-section']
        ]
      },
      admin: {
        label: 'Quy trình quản trị',
        title: 'Kiểm soát toàn bộ dữ liệu và phân quyền hệ thống',
        subtitle: 'Admin theo dõi sách, độc giả, nhân viên, tài khoản, phiếu mượn/trả/phạt và các rủi ro dữ liệu.',
        steps: [
          ['01', 'Quản lý dữ liệu lõi', 'Theo dõi sách, độc giả, nhân viên và tài khoản đăng nhập theo từng vai trò.'],
          ['02', 'Giám sát giao dịch', 'Kiểm tra phiếu mượn, phiếu trả, phiếu phạt và các trường hợp quá hạn.'],
          ['03', 'Bảo toàn lịch sử', 'Không xóa cứng dữ liệu đã phát sinh giao dịch; nên khóa, ẩn hoặc đổi trạng thái.']
        ],
        rules: ['Phân quyền đúng Admin/Staff/Customer', 'Khóa tài khoản vi phạm', 'Không xóa sách đã có lịch sử', 'Dùng thống kê để phát hiện bất thường'],
        actions: [
          ['Quản lý sách', '#mgmt-book'],
          ['Quản lý nhân viên', '#mgmt-staff'],
          ['Phiếu phạt', '#mgmt-fine']
        ]
      }
    };
    return guides[role];
  }

  function buildBusinessGuideMarkup(guide) {
    var steps = guide.steps.map(function (step) {
      return '<article class="biz-step"><span>' + escapeHtml(step[0]) + '</span><h3>' + escapeHtml(step[1]) + '</h3><p>' + escapeHtml(step[2]) + '</p></article>';
    }).join('');

    var rules = guide.rules.map(function (rule) {
      return '<li>' + escapeHtml(rule) + '</li>';
    }).join('');

    var actions = guide.actions.map(function (action) {
      return '<button type="button" data-target="' + escapeHtml(action[1]) + '">' + escapeHtml(action[0]) + '</button>';
    }).join('');

    setTimeout(function () {
      document.querySelectorAll('.role-business-guide button[data-target]').forEach(function (button) {
        button.addEventListener('click', function () {
          var target = document.querySelector(button.getAttribute('data-target'));
          if (!target) return;
          target.scrollIntoView({ behavior: 'smooth', block: 'start' });
          target.classList.add('section-highlight');
          setTimeout(function () { target.classList.remove('section-highlight'); }, 2200);
        });
      });
    }, 0);

    return [
      '<section class="role-business-guide">',
      '  <div class="biz-guide-copy">',
      '    <span class="biz-label">' + escapeHtml(guide.label) + '</span>',
      '    <h2>' + escapeHtml(guide.title) + '</h2>',
      '    <p>' + escapeHtml(guide.subtitle) + '</p>',
      '    <div class="biz-actions">' + actions + '</div>',
      '  </div>',
      '  <div class="biz-steps">' + steps + '</div>',
      '  <aside class="biz-rules">',
      '    <strong>Quy tắc cần nhớ</strong>',
      '    <ul>' + rules + '</ul>',
      '  </aside>',
      '</section>'
    ].join('');
  }

  function enhanceBusinessTables() {
    var statusMap = {
      pending: ['Chờ duyệt', 'pending'],
      approved: ['Đã duyệt', 'approved'],
      borrowed: ['Đang mượn', 'borrowed'],
      overdue: ['Quá hạn', 'overdue'],
      returned: ['Đã trả', 'returned'],
      rejected: ['Từ chối', 'rejected'],
      damaged: ['Hư hỏng', 'damaged'],
      lost: ['Mất sách', 'lost'],
      '0': ['Chưa đóng', 'pending'],
      '1': ['Đã đóng', 'returned'],
      '-1': ['Đã hủy', 'rejected']
    };

    document.querySelectorAll('.badge,.status,.tag').forEach(function (badge) {
      var raw = normalizeText(badge.textContent);
      if (!raw || !statusMap[raw]) return;
      badge.textContent = statusMap[raw][0];
      badge.classList.add('status-' + statusMap[raw][1]);
      badge.setAttribute('title', statusMap[raw][0]);
    });

    document.querySelectorAll('.table-wrap table').forEach(function (table) {
      var body = table.tBodies && table.tBodies[0];
      if (!body) return;
      var visibleRows = Array.prototype.filter.call(body.rows, function (row) {
        return row.textContent.trim().length > 0;
      });
      if (visibleRows.length > 0) return;
      var cols = table.tHead && table.tHead.rows[0] ? table.tHead.rows[0].cells.length : 1;
      var row = body.insertRow();
      var cell = row.insertCell();
      cell.colSpan = cols;
      cell.innerHTML = '<div class="smart-empty-state">Hiện chưa có dữ liệu phù hợp.</div>';
    });
  }

  function initSmartChatbot() {
    var role = detectRole();
    if (!role && document.body.classList.contains('auth-luxury-page')) role = 'auth';
    if (!role) return;

    var config = getRoleConfig(role);
    var storageKey = 'qltv-chat-history-' + role;
    var history = loadHistory(storageKey);

    document.body.classList.add('has-lux-chatbot', 'role-' + role);
    document.body.insertAdjacentHTML('beforeend', buildChatMarkup(config));

    var widget = document.querySelector('.lux-chatbot');
    var toggle = widget.querySelector('.lux-chat-toggle');
    var panel = widget.querySelector('.lux-chat-panel');
    var messages = widget.querySelector('.lux-chat-messages');
    var input = widget.querySelector('.lux-chat-input');
    var sendBtn = widget.querySelector('.lux-chat-send');
    var clearBtn = widget.querySelector('.lux-chat-clear');
    var minimizeBtn = widget.querySelector('.lux-chat-minimize');
    var quickWrap = widget.querySelector('.lux-chat-quick');

    renderHistory(messages, history, config);
    renderQuickButtons(quickWrap, config.quick);

    toggle.addEventListener('click', function () {
      widget.classList.toggle('is-open');
      if (widget.classList.contains('is-open')) input.focus();
    });

    minimizeBtn.addEventListener('click', function () {
      widget.classList.remove('is-open');
    });

    clearBtn.addEventListener('click', function () {
      history = [];
      saveHistory(storageKey, history);
      renderHistory(messages, history, config);
      toast('Đã xóa lịch sử chat');
    });

    sendBtn.addEventListener('click', function () {
      sendMessage();
    });

    input.addEventListener('keydown', function (event) {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
      }
    });

    quickWrap.addEventListener('click', function (event) {
      var btn = event.target.closest('button[data-question]');
      if (!btn) return;
      input.value = btn.getAttribute('data-question');
      sendMessage();
    });

    function sendMessage() {
      var text = input.value.trim();
      if (!text) return;
      input.value = '';
      pushMessage('user', text);
      showTyping(messages);
      setTimeout(function () {
        hideTyping(messages);
        pushMessage('bot', answerQuestion(role, text, config));
      }, 520);
    }

    function pushMessage(sender, text) {
      var item = { sender: sender, text: text, time: Date.now() };
      history.push(item);
      if (history.length > 40) history = history.slice(history.length - 40);
      saveHistory(storageKey, history);
      appendMessage(messages, item);
      scrollMessages(messages);
    }
  }

  function detectRole() {
    var body = document.body;
    var path = normalizeText(location.pathname);
    var title = normalizeText(document.title || '');
    var bodyText = normalizeText([body.className, body.dataset.role || '', body.id || ''].join(' '));

    if (path.indexOf('admin.jsp') >= 0 || bodyText.indexOf('admin') >= 0 || title.indexOf('admin') >= 0) return 'admin';
    if (path.indexOf('staff.jsp') >= 0 || bodyText.indexOf('staff') >= 0 || title.indexOf('nhan vien') >= 0) return 'staff';
    if (path.indexOf('customer.jsp') >= 0 || bodyText.indexOf('customer') >= 0 || title.indexOf('khach hang') >= 0) return 'customer';
    return null;
  }

  function getRoleConfig(role) {
    var configs = {
      customer: {
        icon: '📚',
        title: 'Trợ lý Khách hàng',
        subtitle: 'Hỗ trợ tìm sách, mượn sách, lịch sử và tài khoản',
        hello: 'Xin chào, tôi là Trợ lý Khách hàng. Bạn có thể hỏi về tìm sách, mượn sách, sách đang mượn, lịch sử mượn, phí phạt, đổi mật khẩu hoặc cập nhật thông tin cá nhân.',
        quick: ['Tìm sách', 'Mượn sách', 'Sách đang mượn', 'Lịch sử mượn', 'Phí phạt', 'Gia hạn']
      },
      staff: {
        icon: '🛎️',
        title: 'Trợ lý Nhân viên',
        subtitle: 'Hỗ trợ duyệt mượn, giao trả sách, quá hạn và phiếu phạt',
        hello: 'Xin chào, tôi là Trợ lý Nhân viên. Bạn có thể hỏi về duyệt yêu cầu, giao sách, trả sách, xử lý quá hạn, sách hư hỏng/mất, tạo phiếu phạt hoặc quản lý sách.',
        quick: ['Duyệt yêu cầu', 'Giao sách', 'Trả sách', 'Sách quá hạn', 'Tạo phiếu phạt', 'Nhắc độc giả']
      },
      admin: {
        icon: '🛡️',
        title: 'Trợ lý Admin',
        subtitle: 'Hỗ trợ tài khoản, phân quyền, nhân viên, thống kê và dữ liệu',
        hello: 'Xin chào, tôi là Trợ lý Admin. Bạn có thể hỏi về quản lý tài khoản, phân quyền, khóa/mở tài khoản, thống kê, quản lý nhân viên, quản lý sách hoặc kiểm tra phiếu mượn/trả/phạt.',
        quick: ['Quản lý tài khoản', 'Phân quyền', 'Khóa tài khoản', 'Thống kê', 'Dữ liệu bất thường', 'Báo cáo phạt']
      },
      auth: {
        icon: '💬',
        title: 'Trợ lý Thư viện',
        subtitle: 'Hỗ trợ đăng nhập, đăng ký tài khoản và vai trò sử dụng',
        hello: 'Xin chào, tôi là Trợ lý Thư viện. Bạn có thể hỏi về cách đăng nhập, đăng ký tài khoản độc giả, phân biệt Admin/Staff/Customer, xử lý lỗi sai mật khẩu hoặc chuẩn bị thông tin trước khi vào hệ thống.',
        quick: ['Cách đăng nhập', 'Đăng ký tài khoản', 'Quên mật khẩu', 'Vai trò người dùng', 'Không đăng nhập được', 'Liên hệ thư viện']
      }
    };
    return configs[role];
  }

  function buildChatMarkup(config) {
    return [
      '<div class="lux-chatbot">',
      '  <button class="lux-chat-toggle" type="button" aria-label="Mở chatbot">',
      '    <span class="lux-chat-avatar">' + config.icon + '</span>',
      '    <span class="lux-chat-pulse"></span>',
      '  </button>',
      '  <section class="lux-chat-panel" aria-label="' + escapeHtml(config.title) + '">',
      '    <header class="lux-chat-header">',
      '      <div class="lux-chat-avatar large">' + config.icon + '</div>',
      '      <div class="lux-chat-heading">',
      '        <strong>' + escapeHtml(config.title) + '</strong>',
      '        <span>' + escapeHtml(config.subtitle) + '</span>',
      '        <small><i></i> Đang trực tuyến</small>',
      '      </div>',
      '      <div class="lux-chat-actions">',
      '        <button type="button" class="lux-chat-clear" title="Xóa lịch sử" aria-label="Xóa lịch sử">⌫</button>',
      '        <button type="button" class="lux-chat-minimize" title="Thu nhỏ" aria-label="Thu nhỏ">−</button>',
      '      </div>',
      '    </header>',
      '    <div class="lux-chat-messages"></div>',
      '    <div class="lux-chat-quick" aria-label="Gợi ý nhanh"></div>',
      '    <div class="lux-chat-compose">',
      '      <input class="lux-chat-input" type="text" placeholder="Nhập câu hỏi..." autocomplete="off">',
      '      <button class="lux-chat-send" type="button" aria-label="Gửi tin nhắn"><span>Gửi</span><i>➜</i></button>',
      '    </div>',
      '  </section>',
      '</div>'
    ].join('');
  }

  function renderQuickButtons(container, items) {
    container.innerHTML = items.map(function (text) {
      return '<button type="button" data-question="' + escapeHtml(text) + '">' + escapeHtml(text) + '</button>';
    }).join('');
  }

  function renderHistory(messages, history, config) {
    messages.innerHTML = '';
    if (!history.length) appendMessage(messages, { sender: 'bot', text: config.hello, time: Date.now() });
    history.forEach(function (item) { appendMessage(messages, item); });
    scrollMessages(messages);
  }

  function appendMessage(messages, item) {
    var row = document.createElement('div');
    row.className = 'lux-chat-msg ' + (item.sender === 'user' ? 'user' : 'bot');
    row.innerHTML = '<div class="lux-chat-bubble">' + formatMessage(item.text) + '</div>';
    messages.appendChild(row);
  }

  function showTyping(messages) {
    var typing = document.createElement('div');
    typing.className = 'lux-chat-msg bot lux-chat-typing';
    typing.innerHTML = '<div class="lux-chat-bubble"><span>Đang nhập</span><b></b><b></b><b></b></div>';
    messages.appendChild(typing);
    scrollMessages(messages);
  }

  function hideTyping(messages) {
    var typing = messages.querySelector('.lux-chat-typing');
    if (typing) typing.remove();
  }

  function scrollMessages(messages) {
    messages.scrollTop = messages.scrollHeight;
  }

  function loadHistory(key) {
    try {
      var data = JSON.parse(localStorage.getItem(key) || '[]');
      return Array.isArray(data) ? data : [];
    } catch (e) {
      return [];
    }
  }

  function saveHistory(key, history) {
    try {
      localStorage.setItem(key, JSON.stringify(history));
    } catch (e) {}
  }

  function answerQuestion(role, question, config) {
    var q = normalizeText(question);
    var rules = roleAnswers[role] || [];
    for (var i = 0; i < rules.length; i += 1) {
      if (rules[i].keys.some(function (key) { return q.indexOf(key) >= 0; })) return rules[i].answer;
    }
    return fallbackAnswer(config);
  }

  var roleAnswers = {
    customer: [
      {
        keys: ['lam sao muon sach', 'muon sach', 'dang ky muon', 'cach muon', 'gui yeu cau muon'],
        answer: 'Bạn vào danh sách sách, tìm sách muốn mượn, nếu trạng thái còn sách thì bấm nút Mượn. Sau khi gửi yêu cầu, nhân viên sẽ duyệt. Khi được duyệt, bạn đến thư viện nhận sách. Thời hạn mượn mặc định là 14 ngày.'
      },
      {
        keys: ['tim sach', 'tim kiem sach', 'sach nao', 'tac gia', 'the loai'],
        answer: 'Bạn vào mục Tìm kiếm & mượn sách, nhập tên sách hoặc tác giả vào ô tìm kiếm rồi bấm Tìm kiếm. Nên kiểm tra thêm thể loại, tác giả và trạng thái còn sách trước khi gửi yêu cầu mượn.'
      },
      {
        keys: ['dieu kien muon', 'du dieu kien', 'co duoc muon', 'han muc muon'],
        answer: 'Điều kiện mượn thường gồm: tài khoản đang hoạt động, sách còn số lượng, bạn không có sách quá hạn nghiêm trọng, và chưa gửi yêu cầu trùng cho cùng một sách. Nếu không chắc, hãy kiểm tra Sách đang mượn và thông báo trên trang.'
      },
      {
        keys: ['sach dang muon', 'dang muon', 'toi dang muon', 'han tra'],
        answer: 'Bạn xem mục Sách đang mượn để biết sách nào đang giữ, trạng thái phiếu và hạn trả. Nếu thấy trạng thái Quá hạn, nên mang sách đến thư viện trả sớm để hạn chế phí phạt.'
      },
      {
        keys: ['lich su muon', 'lich su', 'da tra', 'phieu muon cu'],
        answer: 'Bạn mở mục Lịch sử mượn để xem các phiếu mượn trước đây, ngày mượn, ngày hẹn trả và trạng thái đã trả hoặc chưa trả. Mục này giúp đối chiếu khi có thắc mắc về giao dịch mượn/trả.'
      },
      {
        keys: ['phi phat', 'qua han', 'tre han', 'phat', 'nop phat'],
        answer: 'Phí phạt thường phát sinh khi trả sách quá hạn, làm hư hỏng hoặc mất sách. Bạn kiểm tra trạng thái trong Sách đang mượn hoặc Lịch sử mượn; nếu có phiếu phạt, hãy liên hệ nhân viên thư viện để biết số tiền và cách xử lý.'
      },
      {
        keys: ['gia han', 'xin gia han', 'muon tiep', 'keo dai han tra'],
        answer: 'Nếu muốn gia hạn, bạn nên kiểm tra trước sách đang mượn có quá hạn chưa và liên hệ nhân viên thư viện. Gia hạn chỉ nên được chấp nhận khi tài khoản còn hoạt động, sách không bị người khác đặt trước và chưa phát sinh vi phạm nghiêm trọng.'
      },
      {
        keys: ['doi mat khau', 'mat khau', 'quen mat khau', 'password'],
        answer: 'Bạn vào mục Đổi mật khẩu, nhập mật khẩu hiện tại, mật khẩu mới và nhập lại mật khẩu mới. Mật khẩu mới nên đủ dài, khó đoán và không trùng với tên đăng nhập.'
      },
      {
        keys: ['cap nhat thong tin', 'sua thong tin', 'thong tin ca nhan', 'doi dia chi', 'doi so dien thoai'],
        answer: 'Bạn vào mục Sửa thông tin cá nhân, cập nhật họ tên, địa chỉ, ngày sinh hoặc giới tính rồi bấm Lưu thay đổi. Sau khi lưu, hãy kiểm tra thông báo để biết cập nhật có thành công không.'
      },
      {
        keys: ['het sach', 'sach het', 'khong con sach', 'het hang'],
        answer: 'Nếu sách hết hàng, nút Mượn sẽ không khả dụng hoặc trạng thái hiển thị Hết sách. Bạn có thể tìm sách khác cùng thể loại, quay lại kiểm tra sau, hoặc hỏi nhân viên khi nào sách được trả về.'
      },
      {
        keys: ['vi sao khong muon duoc', 'khong muon duoc', 'loi muon sach', 'muon bi loi', 'khong bam duoc muon'],
        answer: 'Có thể do sách đã hết, bạn đang có sách quá hạn, tài khoản bị khóa, hoặc bạn đã gửi yêu cầu mượn sách đó rồi. Hãy kiểm tra mục Sách đang mượn hoặc liên hệ nhân viên thư viện.'
      }
    ],
    staff: [
      {
        keys: ['duyet muon sach', 'duyet yeu cau', 'chap nhan muon', 'phe duyet'],
        answer: 'Bạn vào mục Yêu cầu mượn sách, kiểm tra thông tin độc giả, sách và số lượng còn lại. Nếu hợp lệ, bấm Duyệt. Khi khách đến nhận sách, bấm Xác nhận giao sách để hệ thống trừ số lượng sách.'
      },
      {
        keys: ['tu choi', 'khong duyet', 'huy yeu cau'],
        answer: 'Bạn chỉ nên từ chối khi yêu cầu không hợp lệ: sách không còn, độc giả bị khóa, có quá hạn nghiêm trọng hoặc thông tin không đúng. Trước khi bấm Từ chối, nên kiểm tra lại mã độc giả và mã sách để tránh xử lý nhầm.'
      },
      {
        keys: ['giao sach', 'xac nhan giao', 'khach den nhan'],
        answer: 'Khi yêu cầu đã được duyệt và khách đến nhận sách, hãy đối chiếu thông tin độc giả và sách, sau đó bấm Xác nhận giao sách. Bước này ghi nhận phiếu đang mượn và trừ số lượng sách khả dụng.'
      },
      {
        keys: ['tra sach', 'xac nhan tra', 'lap phieu tra', 'tra binh thuong'],
        answer: 'Khi độc giả trả sách, mở phiếu mượn tương ứng, kiểm tra tình trạng sách và bấm Trả bình thường nếu sách đạt yêu cầu. Nếu sách hư hỏng hoặc mất, chọn đúng trạng thái để lập xử lý/phí phạt.'
      },
      {
        keys: ['qua han', 'tre han', 'sach qua han', 'xu ly qua han'],
        answer: 'Với sách quá hạn, kiểm tra ngày hẹn trả, số ngày trễ và thông tin độc giả. Nhắc độc giả trả sách, sau đó tạo phiếu phạt nếu quy định thư viện yêu cầu thu phí quá hạn.'
      },
      {
        keys: ['hu hong', 'sach hu', 'mat sach', 'sach mat'],
        answer: 'Nếu sách hư hỏng hoặc mất, chọn thao tác Hư hoặc Mất trên phiếu mượn/trả liên quan. Sau đó lập phiếu phạt với lý do rõ ràng, số tiền phù hợp và trạng thái thanh toán để dễ đối soát.'
      },
      {
        keys: ['quan ly sach', 'them sach', 'sua sach', 'tim sach'],
        answer: 'Bạn vào mục Quản lý sách để tìm kiếm, kiểm tra thông tin sách và số lượng khả dụng. Khi thêm hoặc sửa sách, cần nhập đúng tên sách, tác giả, thể loại và số lượng để tránh khách gửi yêu cầu mượn sai trạng thái.'
      },
      {
        keys: ['tim doc gia', 'doc gia', 'khach hang', 'ma doc gia'],
        answer: 'Hãy tìm độc giả bằng mã độc giả, tên hoặc thông tin liên hệ nếu trang hỗ trợ. Trước khi duyệt/giao/trả sách, nên đối chiếu đúng độc giả để tránh nhầm phiếu.'
      },
      {
        keys: ['tao phieu phat', 'lap phieu phat', 'phieu phat', 'thu phat'],
        answer: 'Bạn vào mục Quản lý phiếu phạt, nhập mã độc giả, phiếu mượn liên quan, lý do phạt, số tiền và ngày lập. Sau khi thu tiền, cập nhật trạng thái phiếu phạt để Admin và Staff dễ theo dõi.'
      },
      {
        keys: ['nhac doc gia', 'goi doc gia', 'lien he doc gia', 'gui thong bao'],
        answer: 'Khi nhắc độc giả, hãy đối chiếu mã phiếu, tên sách, hạn trả và số ngày trễ. Nội dung nên ngắn gọn: tên sách, hạn trả, hướng dẫn mang sách đến thư viện và lưu ý phí phạt nếu tiếp tục quá hạn.'
      }
    ],
    admin: [
      {
        keys: ['quan ly tai khoan', 'tai khoan', 'nguoi dung', 'account'],
        answer: 'Bạn vào mục Quản lý tài khoản để xem, tìm kiếm và cập nhật tài khoản người dùng. Khi xử lý tài khoản đã phát sinh mượn/trả/phạt, nên khóa hoặc đổi trạng thái thay vì xóa cứng để giữ lịch sử dữ liệu.'
      },
      {
        keys: ['phan quyen', 'admin staff customer', 'doi vai tro', 'vai tro', 'role'],
        answer: 'Bạn phân quyền tài khoản theo đúng nhiệm vụ: Admin quản trị hệ thống, Staff xử lý nghiệp vụ thư viện, Customer mượn và theo dõi sách. Chỉ cấp quyền Admin cho người thật sự cần quản trị dữ liệu và tài khoản.'
      },
      {
        keys: ['khoa tai khoan', 'mo tai khoan', 'khoa mo', 'vo hieu hoa'],
        answer: 'Với tài khoản vi phạm hoặc không còn sử dụng, hãy khóa hoặc vô hiệu hóa để chặn đăng nhập nhưng vẫn giữ lịch sử. Khi cần khôi phục, mở lại tài khoản sau khi đã xác minh thông tin người dùng.'
      },
      {
        keys: ['quan ly sach', 'them sach', 'sua sach', 'xoa sach', 'lich su muon'],
        answer: 'Bạn có thể quản lý sách trong mục Quản lý sách. Quy tắc quan trọng: không xóa cứng sách đã có lịch sử mượn/trả/phạt; nên ẩn, khóa trạng thái hoặc đặt số lượng về 0 để tránh mất dữ liệu lịch sử.'
      },
      {
        keys: ['quan ly nhan vien', 'nhan vien', 'them nhan vien', 'xoa nhan vien'],
        answer: 'Bạn vào mục Quản lý nhân viên để thêm, sửa hoặc xóa nhân viên. Với tài khoản đã có lịch sử xử lý phiếu mượn/trả, nên khóa hoặc ẩn thay vì xóa cứng để tránh mất dữ liệu lịch sử.'
      },
      {
        keys: ['thong ke', 'bao cao', 'dashboard', 'so lieu'],
        answer: 'Bạn xem khu vực thống kê để nắm số lượng sách, độc giả, phiếu mượn/trả và các chỉ số vận hành. Khi số liệu bất thường, hãy kiểm tra thêm phiếu mượn, phiếu trả và phiếu phạt liên quan.'
      },
      {
        keys: ['du lieu bat thuong', 'bat thuong', 'sai so lieu', 'lech du lieu'],
        answer: 'Nếu số liệu bất thường, hãy kiểm tra theo thứ tự: trạng thái phiếu mượn, phiếu trả đã ghi nhận chưa, số lượng sách có được cộng/trừ đúng không, và phiếu phạt đã cập nhật trạng thái thu tiền chưa. Không nên sửa trực tiếp database khi chưa xác định nguyên nhân.'
      },
      {
        keys: ['bao cao phat', 'doanh thu phat', 'tien phat', 'phat chua thu'],
        answer: 'Báo cáo phạt nên theo dõi tổng tiền phạt, số phiếu chưa thu, lý do phạt phổ biến và độc giả có nhiều vi phạm. Khi đối soát, so sánh phiếu phạt với phiếu mượn liên quan để tránh thu trùng hoặc bỏ sót.'
      },
      {
        keys: ['phieu muon', 'phieu tra', 'phieu phat', 'kiem tra phieu'],
        answer: 'Bạn có thể kiểm tra phiếu mượn, phiếu trả và phiếu phạt theo mã phiếu, độc giả hoặc sách. Khi chỉnh dữ liệu phiếu, cần giữ nhất quán trạng thái sách, số lượng tồn và nghĩa vụ phạt nếu có.'
      },
      {
        keys: ['khong xoa cung', 'xoa cung', 'du lieu lich su', 'lich su du lieu'],
        answer: 'Không nên xóa cứng sách, nhân viên hoặc tài khoản đã có lịch sử mượn/trả/phạt. Cách an toàn hơn là khóa, ẩn hoặc đánh dấu ngừng sử dụng để bảo toàn báo cáo và dữ liệu đối soát.'
      }
    ],
    auth: [
      {
        keys: ['cach dang nhap', 'dang nhap', 'login', 'vao he thong'],
        answer: 'Bạn nhập tên đăng nhập hoặc email vào ô tài khoản, nhập mật khẩu rồi bấm Đăng nhập. Hệ thống sẽ tự chuyển đến đúng giao diện theo quyền: Admin, Nhân viên hoặc Khách hàng.'
      },
      {
        keys: ['dang ky', 'tao tai khoan', 'register', 'tai khoan moi'],
        answer: 'Nếu chưa có tài khoản, bấm Đăng ký ngay. Tài khoản tự đăng ký sẽ là tài khoản Khách hàng/Độc giả để tìm sách, gửi yêu cầu mượn và xem lịch sử mượn.'
      },
      {
        keys: ['quen mat khau', 'sai mat khau', 'password', 'khong nho mat khau'],
        answer: 'Bản hiện tại chưa gắn chức năng khôi phục mật khẩu tự động. Nếu quên mật khẩu, bạn nên liên hệ nhân viên hoặc quản trị viên thư viện để được kiểm tra và đặt lại tài khoản.'
      },
      {
        keys: ['khong dang nhap duoc', 'loi dang nhap', 'dang nhap loi', 'sai tai khoan'],
        answer: 'Hãy kiểm tra lại tên đăng nhập/email, mật khẩu, trạng thái tài khoản và kết nối database. Nếu vẫn lỗi, thông báo trên màn hình sẽ cho biết tài khoản sai, bị khóa hoặc hệ thống không kết nối được dữ liệu.'
      },
      {
        keys: ['vai tro', 'admin', 'staff', 'customer', 'nhan vien', 'khach hang', 'doc gia'],
        answer: 'Admin quản trị dữ liệu, tài khoản và thống kê. Nhân viên xử lý duyệt mượn, giao/trả sách và phiếu phạt. Khách hàng/Độc giả tìm sách, gửi yêu cầu mượn và theo dõi lịch sử cá nhân.'
      },
      {
        keys: ['can thong tin gi', 'thong tin dang ky', 'email', 'so dien thoai'],
        answer: 'Khi đăng ký, bạn nên nhập họ tên, tên đăng nhập, email hoặc số điện thoại, ngày sinh, giới tính, địa chỉ và mật khẩu. Tên đăng nhập cần không trùng với tài khoản đã có.'
      },
      {
        keys: ['lien he', 'thu vien', 'ho tro', 'gap nhan vien'],
        answer: 'Nếu cần hỗ trợ trực tiếp, hãy liên hệ quầy thư viện hoặc nhân viên quản lý hệ thống. Bạn nên cung cấp tên đăng nhập, họ tên và lỗi đang gặp để được xử lý nhanh hơn.'
      }
    ]
  };

  function fallbackAnswer(config) {
    return config.title + ' có thể hỗ trợ các nội dung như: ' + config.quick.join(', ') + '. Bạn hãy hỏi cụ thể hơn, ví dụ: "' + config.quick[0] + '" hoặc "' + config.quick[1] + '".';
  }

  function normalizeText(text) {
    return String(text || '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/đ/g, 'd')
      .replace(/[^a-z0-9\s]/g, ' ')
      .replace(/\s+/g, ' ')
      .trim();
  }

  function escapeHtml(text) {
    return String(text || '')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }

  function formatMessage(text) {
    return escapeHtml(text).replace(/\n/g, '<br>');
  }

  window.scrollToStaffSection = function (sectionId) {
    var section = document.getElementById(sectionId);
    if (!section) return;
    section.scrollIntoView({ behavior: 'smooth', block: 'start' });
    section.classList.add('section-highlight');
    setTimeout(function () {
      section.classList.remove('section-highlight');
    }, 2200);
  };

  window.normalizeText = window.normalizeText || normalizeText;
})();
