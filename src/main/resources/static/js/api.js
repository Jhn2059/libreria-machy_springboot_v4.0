const API_BASE = '/api';

const api = {
  _token: null,

  setToken(token) { this._token = token; },
  getToken() { return this._token; },

  async _fetch(method, path, body) {
    const headers = { 'Content-Type': 'application/json' };
    if (this._token) headers['Authorization'] = 'Bearer ' + this._token;
    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(API_BASE + path, opts);
    const json = await res.json();
    if (!json.success) throw new Error(json.message || 'Error de red');
    return json.data;
  },

  get(path) { return this._fetch('GET', path); },
  post(path, body) { return this._fetch('POST', path, body); },
  put(path, body) { return this._fetch('PUT', path, body); },
  patch(path, body) { return this._fetch('PATCH', path, body); },
  del(path) { return this._fetch('DELETE', path); },

  // ── Auth ──
  login(username, password) {
    return this.post('/auth/login', { username, password });
  },
  recover(usernameOrEmail) {
    return this.post('/auth/recover', { usernameOrEmail });
  },

  // ── Dashboard ──
  getDashboard() { return this.get('/reports/dashboard'); },

  // ── Products ──
  getProducts(q, categoria) {
    let path = '/products';
    const params = [];
    if (q) params.push('q=' + encodeURIComponent(q));
    if (categoria) params.push('categoria=' + encodeURIComponent(categoria));
    if (params.length) path += '?' + params.join('&');
    return this.get(path);
  },
  getActiveProducts() { return this.get('/products/active'); },
  getProduct(id) { return this.get('/products/' + id); },
  getProductByCode(code) { return this.get('/products/by-code/' + encodeURIComponent(code)); },
  createProduct(data) { return this.post('/products', data); },
  updateProduct(id, data) { return this.put('/products/' + id, data); },
  toggleProduct(id) { return this.patch('/products/' + id + '/toggle'); },

  // ── Sales ──
  getSales() { return this.get('/sales'); },
  getSale(id) { return this.get('/sales/' + id); },
  createSale(data) { return this.post('/sales', data); },
  cancelSale(id, motivo) { return this.post('/sales/' + id + '/cancel', { motivo }); },

  // ── Users ──
  getUsers() { return this.get('/users'); },
  getUser(id) { return this.get('/users/' + id); },
  createUser(data) { return this.post('/users', data); },
  updateUser(id, data) { return this.put('/users/' + id, data); },
  toggleUser(id) { return this.patch('/users/' + id + '/toggle'); },
  getUserDashboard() { return this.get('/users/dashboard'); },

  // ── Attendance ──
  getAttendanceStatus() { return this.get('/users/attendance/status'); },
  checkIn() { return this.post('/users/attendance/check-in'); },
  checkOut() { return this.post('/users/attendance/check-out'); },
  adminAttendance(usuarioId, tipo) { return this.post('/users/attendance/admin', { usuarioId, tipo }); },

  // ── Categories ──
  getCategories() { return this.get('/categories'); },
  createCategory(nombre) { return this.post('/categories', { nombre }); },
  deleteCategory(id) { return this.del('/categories/' + id); },

  // ── Suppliers ──
  getSuppliers() { return this.get('/suppliers'); },
  createSupplier(data) { return this.post('/suppliers', data); },
  deleteSupplier(id) { return this.del('/suppliers/' + id); },

  // ── Config ──
  getConfig() { return this.get('/config'); },
  saveConfig(data) { return this.put('/config', data); },

  // ── Reports ──
  getSalesReport() { return this.get('/reports/sales'); },
  getInventoryReport() { return this.get('/reports/inventory'); },
  getAttendanceReport() { return this.get('/reports/attendance'); },

  // ── Scan Session ──
  createScanSession() { return this.post('/scan/session'); },
  endScanSession(sessionId) { return this.del('/scan/session/' + sessionId); },
};
