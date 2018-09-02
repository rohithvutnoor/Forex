import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionService } from './session.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  apisJsonFilePath = "assets/apis.json";
  apis = {
    "base_url":"http://localhost:8080/backend",
    "signin": {
        "url":"/signin"
    },
    "registration": {
        "url":"/registration"
    },
    "order_blotter": {
      "url": "/blotter"
    },
    "trade_blotter": {
      "url": "/blotter"
    },
    "currency_pair_prices": {
      "url": "/currency-pair-price"
    },
    "currency_pairs": {
      "url": "/currency-pair"
    },
    "sell": {
      "url": "/sell"
    },
    "buy": {
      "url": "/buy"
    },
    "positions": {
      "url": "/positions"
    }
  };

  constructor(
    private http: HttpClient,
    private session: SessionService
  ) {}

  postRequest(url:string, body:any) {
    const options = {
      "headers": new HttpHeaders({"Authorization": "Bearer " + this.session.accessToken})
    };
    return this.http.post(this.apis["base_url"] + url, body, options);
  }

  getRequest(url:String, params:any) {
    let queryParams = new HttpParams();
    if(params != undefined && params != null) {
      for (let param in params) {
        queryParams = queryParams.append(param, params[param]);
      }
    }
    const options = {
      "headers": new HttpHeaders({"Authorization": "Bearer " + this.session.accessToken}),
      "params": queryParams
    };
    return this.http.get(this.apis["base_url"] + url, options);
  }

}
