import MyProfile from "./components/MyProfile.js";

const navigateTo = (url) => {
    history.pushState(null, null, url);
    router();
};

const router = async () => {
    const routes = [
        { path: "/my-profile", view: MyProfile }
    ];
    const potentialMatches = routes.map((route) => {
        return {
            route,
            isMatch: location.pathname === route.path
        };
    });
    let match = potentialMatches.find((potentialMatch) => potentialMatch.isMatch);
    if (!match) {
        match = {
            route: routes[0],
            isMatch: true
        };
    }

    const view = new match.route.view();
    document.querySelector("#content-container").innerHTML = await view.getHtml();
};

document.addEventListener("DOMContentLoaded", async function (){
    loadUserProfile();
    router();
});

document.body.addEventListener('click', (e) => {
    if (e.target.matches('[data-link]')) {
        e.preventDefault();
        loadUserProfile();
        navigateTo(e.target.href);
    }
});

window.addEventListener("popstate", router)