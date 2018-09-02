import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import { SessionService } from '../session.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  
  public activeTab = 'current_prices';

  user: JSON;
 // userImage: '../../assets/images/rohith.jpg';
  constructor(
    private auth: AuthService,
    private session: SessionService,
    private router:Router
  ) { 
    this.user = session.user;
    console.log(this.user);
    this.activeTab = 'current_prices';
  }

  ngOnInit() {
  }

  signout() {
    this.auth.doSignOut();
    this.router.navigate(['']);
  }

}
