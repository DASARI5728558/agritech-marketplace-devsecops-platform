/**
 * AgriTech Marketplace — shared frontend behaviour.
 * Handles: catalog rendering/filtering/search, product modal,
 * cart persistence (localStorage), and cart badge sync across pages.
 */

const CART_KEY = "agritech_cart_v1";
const FALLBACK_IMG =
  "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=600&q=80";

/* ---------------- Cart storage helpers ---------------- */

function readCart() {
  try {
    const raw = localStorage.getItem(CART_KEY);
    return raw ? JSON.parse(raw) : {};
  } catch (err) {
    console.warn("Could not read cart from storage", err);
    return {};
  }
}

function writeCart(cart) {
  try {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
  } catch (err) {
    console.warn("Could not persist cart to storage", err);
  }
  syncCartCount();
}

function addToCart(productId, qty) {
  const cart = readCart();
  cart[productId] = (cart[productId] || 0) + qty;
  writeCart(cart);
}

function setCartQty(productId, qty) {
  const cart = readCart();
  if (qty <= 0) {
    delete cart[productId];
  } else {
    cart[productId] = qty;
  }
  writeCart(cart);
}

function removeFromCart(productId) {
  const cart = readCart();
  delete cart[productId];
  writeCart(cart);
}

function cartItemCount() {
  const cart = readCart();
  return Object.values(cart).reduce((sum, qty) => sum + qty, 0);
}

function syncCartCount() {
  document.querySelectorAll("#cartCount").forEach((el) => {
    el.textContent = cartItemCount();
  });
}

/* ---------------- Catalog (index.html) ---------------- */

function findProduct(id) {
  return typeof PRODUCTS !== "undefined" ? PRODUCTS.find((p) => p.id === id) : null;
}

function formatPrice(value) {
  return "₹" + Number(value).toLocaleString("en-IN");
}

function productCardHtml(product) {
  const badge = product.organic
    ? `<span class="card-badge organic"><i class="fa-solid fa-leaf"></i> Organic</span>`
    : `<span class="card-badge"><i class="fa-solid fa-tag"></i> ${CATEGORY_LABELS[product.category] || ""}</span>`;

  return `
    <article class="card" data-id="${product.id}">
      <div class="card-media">
        ${badge}
        <img src="${product.image}" alt="${product.name}"
             onerror="this.onerror=null;this.src='${FALLBACK_IMG}';" />
      </div>
      <div class="card-body">
        <h3>${product.name}</h3>
        <p>${product.description}</p>
        <div class="card-meta">
          <div class="card-price">${formatPrice(product.price)} <span>/ ${product.unit}</span></div>
        </div>
      </div>
      <div class="card-actions">
        <button class="btn btn-secondary" data-action="view" data-id="${product.id}">
          <i class="fa-solid fa-eye"></i> View
        </button>
        <button class="btn btn-primary" data-action="add" data-id="${product.id}">
          <i class="fa-solid fa-cart-plus"></i> Add
        </button>
      </div>
    </article>
  `;
}

function renderCatalog(filterCategory, searchTerm) {
  const grid = document.getElementById("productGrid");
  if (!grid || typeof PRODUCTS === "undefined") return;

  const term = (searchTerm || "").trim().toLowerCase();
  const filtered = PRODUCTS.filter((p) => {
    const matchesCategory = !filterCategory || filterCategory === "all" || p.category === filterCategory;
    const haystack = (p.name + " " + p.description + " " + (p.tags || []).join(" ")).toLowerCase();
    const matchesSearch = !term || haystack.includes(term);
    return matchesCategory && matchesSearch;
  });

  if (filtered.length === 0) {
    grid.innerHTML = `
      <div class="empty-state">
        <i class="fa-solid fa-magnifying-glass"></i>
        <p>No products match your search. Try a different keyword or category.</p>
      </div>`;
    return;
  }

  grid.innerHTML = filtered.map(productCardHtml).join("");
}

function openProductModal(productId) {
  const product = findProduct(productId);
  const modal = document.getElementById("productModal");
  const body = document.getElementById("modalBody");
  if (!product || !modal || !body) return;

  let qty = 1;

  const render = () => {
    body.innerHTML = `
      <div class="modal-hero">
        <img src="${product.image}" alt="${product.name}"
             onerror="this.onerror=null;this.src='${FALLBACK_IMG}';" />
      </div>
      <div class="modal-content">
        <h2>${product.name}</h2>
        <div class="modal-price">${formatPrice(product.price)} <span>/ ${product.unit}</span></div>
        <p class="desc">${product.description}</p>
        <div class="modal-tags">
          ${product.organic ? '<span><i class="fa-solid fa-leaf"></i> Organic</span>' : ""}
          ${(product.tags || []).map((t) => `<span>${t}</span>`).join("")}
        </div>
        <div class="modal-qty">
          <button type="button" id="qtyMinus" aria-label="Decrease quantity">−</button>
          <span id="qtyValue">${qty}</span>
          <button type="button" id="qtyPlus" aria-label="Increase quantity">+</button>
        </div>
        <button class="btn btn-primary btn-block" id="modalAddBtn">
          <i class="fa-solid fa-cart-plus"></i> Add to cart
        </button>
      </div>
    `;

    document.getElementById("qtyMinus").addEventListener("click", () => {
      qty = Math.max(1, qty - 1);
      document.getElementById("qtyValue").textContent = qty;
    });
    document.getElementById("qtyPlus").addEventListener("click", () => {
      qty += 1;
      document.getElementById("qtyValue").textContent = qty;
    });
    document.getElementById("modalAddBtn").addEventListener("click", () => {
      addToCart(product.id, qty);
      closeProductModal();
    });
  };

  render();
  modal.classList.remove("hidden");
  modal.setAttribute("aria-hidden", "false");
}

function closeProductModal() {
  const modal = document.getElementById("productModal");
  if (!modal) return;
  modal.classList.add("hidden");
  modal.setAttribute("aria-hidden", "true");
}

function initCatalogPage() {
  const grid = document.getElementById("productGrid");
  if (!grid) return;

  let activeFilter = "all";
  let activeSearch = "";

  renderCatalog(activeFilter, activeSearch);

  const filterBar = document.getElementById("filterBar");
  if (filterBar) {
    filterBar.addEventListener("click", (e) => {
      const chip = e.target.closest(".chip");
      if (!chip) return;
      filterBar.querySelectorAll(".chip").forEach((c) => c.classList.remove("active"));
      chip.classList.add("active");
      activeFilter = chip.dataset.filter;
      renderCatalog(activeFilter, activeSearch);
    });
  }

  const searchInput = document.getElementById("searchInput");
  if (searchInput) {
    searchInput.addEventListener("input", (e) => {
      activeSearch = e.target.value;
      renderCatalog(activeFilter, activeSearch);
    });
  }

  grid.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    const id = btn.dataset.id;
    if (btn.dataset.action === "view") {
      openProductModal(id);
    } else if (btn.dataset.action === "add") {
      addToCart(id, 1);
      btn.innerHTML = '<i class="fa-solid fa-check"></i> Added';
      setTimeout(() => {
        btn.innerHTML = '<i class="fa-solid fa-cart-plus"></i> Add';
      }, 1200);
    }
  });

  const modal = document.getElementById("productModal");
  const closeBtn = document.getElementById("closeModal");
  if (closeBtn) closeBtn.addEventListener("click", closeProductModal);
  if (modal) {
    modal.addEventListener("click", (e) => {
      if (e.target === modal) closeProductModal();
    });
  }
}

/* ---------------- Cart page (pages/cart.html) ---------------- */

function cartItemHtml(product, qty) {
  return `
    <div class="cart-item" data-id="${product.id}">
      <img src="${product.image}" alt="${product.name}"
           onerror="this.onerror=null;this.src='${FALLBACK_IMG}';" />
      <div class="cart-item-info">
        <h4>${product.name}</h4>
        <span>${formatPrice(product.price)} / ${product.unit}</span>
      </div>
      <div class="cart-item-qty">
        <button type="button" data-action="dec" data-id="${product.id}">−</button>
        <span>${qty}</span>
        <button type="button" data-action="inc" data-id="${product.id}">+</button>
      </div>
      <div class="cart-item-price">${formatPrice(product.price * qty)}</div>
      <button class="cart-item-remove" data-action="remove" data-id="${product.id}" aria-label="Remove item">
        <i class="fa-solid fa-trash"></i>
      </button>
    </div>
  `;
}

function renderCartPage() {
  const itemsEl = document.getElementById("cartItems");
  const layoutEl = document.getElementById("cartLayout");
  const emptyEl = document.getElementById("cartEmpty");
  if (!itemsEl || typeof PRODUCTS === "undefined") return;

  const cart = readCart();
  const entries = Object.entries(cart).filter(([id]) => findProduct(id));

  if (entries.length === 0) {
    if (layoutEl) layoutEl.classList.add("hidden");
    if (emptyEl) emptyEl.classList.remove("hidden");
    return;
  }

  if (layoutEl) layoutEl.classList.remove("hidden");
  if (emptyEl) emptyEl.classList.add("hidden");

  itemsEl.innerHTML = entries
    .map(([id, qty]) => cartItemHtml(findProduct(id), qty))
    .join("");

  let subtotal = 0;
  entries.forEach(([id, qty]) => {
    subtotal += findProduct(id).price * qty;
  });
  const deliveryFee = subtotal > 0 ? 49 : 0;
  const total = subtotal + deliveryFee;

  const subtotalEl = document.getElementById("cartSubtotal");
  const deliveryEl = document.getElementById("cartDelivery");
  const totalEl = document.getElementById("cartTotal");
  if (subtotalEl) subtotalEl.textContent = formatPrice(subtotal);
  if (deliveryEl) deliveryEl.textContent = formatPrice(deliveryFee);
  if (totalEl) totalEl.textContent = formatPrice(total);
}

function initCartPage() {
  const itemsEl = document.getElementById("cartItems");
  if (!itemsEl) return;

  renderCartPage();

  itemsEl.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    const id = btn.dataset.id;
    const cart = readCart();
    const currentQty = cart[id] || 0;

    if (btn.dataset.action === "inc") {
      setCartQty(id, currentQty + 1);
    } else if (btn.dataset.action === "dec") {
      setCartQty(id, currentQty - 1);
    } else if (btn.dataset.action === "remove") {
      removeFromCart(id);
    }
    renderCartPage();
  });

  const checkoutBtn = document.getElementById("checkoutBtn");
  if (checkoutBtn) {
    checkoutBtn.addEventListener("click", () => {
      const cart = readCart();
      if (Object.keys(cart).length === 0) return;
      localStorage.removeItem(CART_KEY);
      syncCartCount();
      renderCartPage();
      const msg = document.getElementById("checkoutMsg");
      if (msg) {
        msg.classList.add("show");
        setTimeout(() => msg.classList.remove("show"), 4000);
      }
    });
  }
}

/* ---------------- Auth forms (login / register — demo only) ---------------- */

function initAuthForm(formId, msgId) {
  const form = document.getElementById(formId);
  const msg = document.getElementById(msgId);
  if (!form) return;

  form.addEventListener("submit", (e) => {
    e.preventDefault();
    if (msg) {
      msg.textContent = "Demo only — this form does not create a real account yet.";
      msg.classList.remove("error");
      msg.classList.add("show", "success");
    }
  });
}

/* ---------------- Boot ---------------- */

document.addEventListener("DOMContentLoaded", () => {
  syncCartCount();
  initCatalogPage();
  initCartPage();
  initAuthForm("loginForm", "loginMsg");
  initAuthForm("registerForm", "registerMsg");
});
