import AbstractView from "./AbstractView.js";

export default class extends AbstractView {
    constructor() {
        super();
        this.setTitle("MyProfile");
    }

    async getHtml() {
        return `
            <div class="col-lg-8 mx-auto p-4 py-md-5">
                <header class="d-flex align-items-center pb-3 mb-5 border-bottom">
                    <span class="fs-4">나의 프로필</span>
                </header>

                <main>
                    <div class="row g-5">
                        <div class="col-md-4">
                            <img src="" id="p-img" alt="프로필 이미지" class="prof-img">
                            <h4 class="text-body-emphasis mt-4">FOLLOWER: <span id="follower-count"></span></h4>
                            <h4 class="text-body-emphasis">FOLLOWING: <span id="following-count"></span></h4>
                        </div>

                        <div class="col-md-6">
                            <h4 class="text-body-emphasis">아이디: <span id="username"></span></h4>
                            <h4 class="text-body-emphasis">이름: <span id="real-name"></span></h4>
                            <h4 class="text-body-emphasis">이메일: <span id="email"></span></h4>
                            <h4 class="text-body-emphasis">작성한 게시글 수: <span id="written-articles"></span></h4>
                            <h4 class="text-body-emphasis">작성한 댓글 수: <span id="written-comments"></span></h4>
                        </div>
                    </div>
                </main>
            </div>
        `;
    }
}