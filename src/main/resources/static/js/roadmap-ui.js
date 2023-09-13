window.addEventListener('DOMContentLoaded', displayRoadmapId);

function displayRoadmapId() {
    const roadmapId = getRoadmapIdFromUrl();
    document.querySelector('#roadmapId').textContent = "roadmapId: " + roadmapId;
}

function getRoadmapIdFromUrl() {
    const regex = /\/roadmaps\/(\d+)/;
    const match = window.location.pathname.match(regex);
    return match[1];
}