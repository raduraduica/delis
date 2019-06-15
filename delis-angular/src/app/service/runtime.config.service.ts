import { Injectable } from "@angular/core";
import { HttpRestService } from "./http.rest.service";
import { environment } from "../../environments/environment";

@Injectable()
export class RuntimeConfigService {

    private env = environment;
    private url = this.env.api_url;
    private config: string;
    private LOCALE_URL = "url";
    private LOCALE_USERNAME = "username";

    constructor( private http: HttpRestService ) {
    }

    getUrl() {
        this.http.methodInnerGet('assets/config/runtime.json').subscribe(
            (data: {}) => {
                localStorage.setItem(this.LOCALE_URL, data["PARAM_API_URL"]);
            }
        );
    }

    getConfigUrl() : string {
        this.config = localStorage.getItem(this.LOCALE_URL);
        if (this.config !== '${API_URL}') {
            return this.config;
        } else {
            return this.url;
        }
    }

    getCurrentUser() : string {
         return localStorage.getItem(this.LOCALE_USERNAME);
    }

    resetCurrentUser() {
        localStorage.removeItem(this.LOCALE_USERNAME);
    }
}
