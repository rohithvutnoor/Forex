import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public accessToken: string;
  public name: string;
  public user: JSON;

  constructor() {
    this.accessToken = null;
   }

  public destroy() {
    this.accessToken = null;
    this.name = null;
  }

}
