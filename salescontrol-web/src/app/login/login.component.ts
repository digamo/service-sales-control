import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { User } from './user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent  {

  username : string;
  password : string;
  signUp : boolean = false;
  messageSuccess : string;
  errors : string [];

  constructor(
    private router : Router,
    private authService : AuthService) { }

  onSubmit(){
    const user = new User();
    user.password = this.password;
    user.username = this.username;

    this.authService.validateToken(user)
      .subscribe(response => {
        const access_token = JSON.stringify(response);
        localStorage.setItem('access_token', access_token);
        this.router.navigate(['home']);
      }, errorResponse => {
        
        this.errors = ["Usuário e/ou Senha incorretos."];    

      })

  }

  prepareToSignUp(event){
    event.preventDefault();
    this.signUp = true;
    this.messageSuccess = "";
    this.errors = [];
  }

  backToLogin(){
    this.signUp = false;
    this.errors = [];
    this.username = "";
    this.password = "";
  }

  register(){
    const user = new User();
    user.password = this.password;
    user.username = this.username;
    this.authService.save(user)
      .subscribe( response => {
          this.messageSuccess = "Cadastro realizado com sucesso. Efetue o login.";
          this.errors = [];
          this.signUp = false;
          this.username = "";
          this.password = "";
      }, errorResponse => {
        this.errors = errorResponse.error;
        this.messageSuccess = null;
      })
  }
}
