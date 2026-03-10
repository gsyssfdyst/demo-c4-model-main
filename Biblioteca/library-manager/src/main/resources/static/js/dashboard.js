/* ============================================
   DASHBOARD - JAVASCRIPT
   ============================================ */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize Bootstrap tooltips
    initTooltips();
    
    // Handle navigation on mobile
    setupMobileNavigation();
});

function initTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

function setupMobileNavigation() {
    // Add mobile menu toggle functionality if needed
    const sidebar = document.querySelector('.sidebar');
    if (!sidebar) return;
    
    // You can add mobile menu toggle here
}

// Utility function to format currency
function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
}

// Utility function to format date
function formatDate(date) {
    return new Intl.DateTimeFormat('pt-BR').format(new Date(date));
}

// Export functions for use in templates
window.Dashboard = {
    formatCurrency,
    formatDate
};
