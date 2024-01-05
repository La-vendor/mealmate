// script.js

document.addEventListener('DOMContentLoaded', function() {
    const fetchMenusButton = document.getElementById('fetchMenus');
    const menuContainer = document.getElementById('menuContainer');

    fetchMenusButton.addEventListener('click', function() {
        fetch('/daily-menu/all') // Replace this with your backend URL
            .then(response => response.json())
            .then(data => {
                // Clear previous content
                menuContainer.innerHTML = '';

                if (data.length > 0) {
                    data.forEach(menu => {
                        const menuElement = document.createElement('div');
                        menuElement.textContent = `Menu ID: ${menu.id}, Name: ${menu.name}, Description: ${menu.description}`;
                        menuContainer.appendChild(menuElement);
                    });
                } else {
                    menuContainer.textContent = 'No daily menus found.';
                }
            })
            .catch(error => {
                console.error('Error fetching daily menus:', error);
                menuContainer.textContent = 'An error occurred while fetching daily menus.';
            });
    });
});
