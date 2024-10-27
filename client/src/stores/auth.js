// src/stores/auth.js
import { writable } from 'svelte/store';

// Initialize the authentication state based on whether a token exists in localStorage
const tokenExists = localStorage.getItem('access_token') !== null;
export const isAuthenticated = writable(tokenExists);

// Subscribe to changes in isAuthenticated and update localStorage as needed
isAuthenticated.subscribe((value) => {
    if (!value) {
        // If logged out, remove the token from localStorage
        localStorage.removeItem('access_token');
    }
});
