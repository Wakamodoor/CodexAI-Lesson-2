<template>
  <header class="app-header">
    <div class="header-content">
      <div class="logo-container">
        <!-- Simple text logo, could be replaced with an SVG or image -->
        <RouterLink to="/" class="logo-link">BM</RouterLink>
        <h1>Bookmark Manager</h1>
      </div>
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <!-- Example: <RouterLink to="/about">About</RouterLink> -->
      </nav>
    </div>
  </header>
  <main class="app-main">
    <RouterView />
  </main>
  <footer class="app-footer">
    <p>&copy; {{ new Date().getFullYear() }} Bookmark Manager. All rights reserved.</p>
  </footer>
</template>

<style scoped>
.app-header {
  background-color: var(--primary-color);
  color: var(--white);
  padding: 0.75rem 2rem; /* Consistent with #app padding */
  position: fixed; /* Fixed header */
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000; /* Ensure it's above other content */
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1200px; /* Match #app max-width */
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-link {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--white);
  text-decoration: none;
  margin-right: 0.5rem; /* Space between icon/text-logo and title */
  padding: 0.25rem 0.5rem;
  border: 1px solid var(--white);
  border-radius: var(--border-radius);
}
.logo-link:hover {
  background-color: rgba(255,255,255,0.1);
}


.app-header h1 {
  font-size: 1.5rem; /* Reduced size for header */
  margin: 0;
  color: var(--white);
  font-weight: 500;
}

nav a {
  color: var(--white);
  text-decoration: none;
  padding: 0.5rem 1rem;
  margin-left: 0.5rem;
  border-radius: var(--border-radius);
  transition: background-color 0.2s ease;
}

nav a:hover {
  background-color: var(--primary-color-dark);
  text-decoration: none;
}

nav a.router-link-exact-active {
  background-color: var(--primary-color-dark);
  /* font-weight: bold; */
}

.app-main {
  /* padding-top: 60px; This is now handled by body padding in main.css */
  /* The body padding ensures content isn't hidden by fixed header */
  padding-bottom: 60px; /* Space for the footer */
}

.app-footer {
  background-color: var(--light-gray);
  color: var(--text-color-muted);
  text-align: center;
  padding: 1rem;
  border-top: 1px solid var(--medium-gray);
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  font-size: 0.875rem;
  z-index: 999; /* Below header but above page scroll */
}


/* Responsive adjustments for header */
@media (max-width: 768px) {
  .app-header {
    padding: 0.5rem 1rem; /* Reduced padding for smaller screens */
  }
  .app-header h1 {
    font-size: 1.2rem; /* Further reduce for very small screens */
  }
  .logo-link {
    font-size: 1.2rem;
    padding: 0.15rem 0.3rem;
  }
  nav a {
    padding: 0.4rem 0.6rem;
    margin-left: 0.25rem;
  }
   .app-main {
    padding-bottom: 50px; /* Adjust for smaller footer */
  }
  .app-footer {
    font-size: 0.8rem;
  }
}
</style>
