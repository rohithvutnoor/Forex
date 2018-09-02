import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import {Router} from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public formGroup: FormGroup;
  
  public isBusy =false;
  public hasFailed = false;
  public showInputErrors = false;

  response:JSON;
  errors:JSON;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) { 
    this.formGroup = this.formBuilder.group ({
      email: ['', Validators.compose( [Validators.required, Validators.email] ) ],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
  }
  
  login() {
    if(this.formGroup.invalid) {
      this.showInputErrors = true;
      return;
    }
    this.isBusy = true;
    this.hasFailed = false;

    let loginUrl = this.apiService.apis.signin.url;
    let body = {
      "email": this.formGroup.get("email").value,
      "password": this.formGroup.get("password").value
    }
    this.apiService.postRequest(loginUrl, body).subscribe(
      (data:any) => {
        this.response=data;
        this.authService.doSignIn(
          this.response["user"]["token"], 
          this.response["user"]["firstname"] + " " + this.response["user"]["lastname"],
          this.response["user"]
        );
        this.router.navigate(['dashboard']);
      }, 
      (error:any) => {
        this.isBusy = false;
        this.hasFailed = true;
        this.errors = error;
      }
    );
  }

}
