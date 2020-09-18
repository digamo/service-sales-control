import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  username: string;

  constructor(
    private authService : AuthService,
    private router : Router
  ) { }

  ngOnInit(): void {
    this.username = this.authService.getAuthenticatedUser();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
