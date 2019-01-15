import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { routerTransition } from '../router.animations';
import { AuthorizationService } from './authorization.service';
import { LocaleService } from "../service/locale.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    animations: [routerTransition()]
})
export class LoginComponent implements OnInit {

    login: string;
    password: string;

    constructor(
        private auth: AuthorizationService,
        private translate: TranslateService,
        private locale: LocaleService,
        public router: Router) {
        this.translate.use(locale.getlocale().match(/en|da/) ? locale.getlocale() : 'en');
    }

    ngOnInit() {
    }

    onLoggedin() {
        this.auth.login(this.login, this.password).subscribe(
            () => {
                localStorage.setItem('isLoggedin', 'true');
            }
        );
    }
}
