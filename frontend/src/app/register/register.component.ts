import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import {Router} from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  public formGroup: FormGroup;
  
  public isBusy =false;
  public hasFailed = false;
  public showInputErrors = false;

  response:JSON;
  errors:JSON;

  constructor(
    private apiService: ApiService,
    private formBuilder: FormBuilder,
    private router: Router
  ) { 
    this.formGroup = this.formBuilder.group ({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', Validators.compose( [Validators.required, Validators.email] ) ],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  register(firstname:String, lastname:String, email:String, password:String) {
    if(this.formGroup.invalid) {
      this.showInputErrors = true;
      return;
    }
    this.isBusy = true;
    this.hasFailed = false;

    let registrationUrl = this.apiService.apis.registration.url;
    let body = {
      "firstname": this.formGroup.get("firstname").value,
      "lastname": this.formGroup.get("lastname").value,
      "email": this.formGroup.get("email").value,
      "password": this.formGroup.get("password").value
    }
    this.apiService.postRequest(registrationUrl, body).subscribe(
      (data:JSON) => {
        this.response=data;
        this.router.navigate(['login']);
      }, 
      (error:any) => {
        this.isBusy = false;
        this.hasFailed = true;
        this.errors = error;
      }
    );
  }

}
