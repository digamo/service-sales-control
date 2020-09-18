import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../environments/environment';
import { User } from '../app/login/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  apiURL: string = environment.apiUrlBase + '/api/users';
  tokenURL: string = environment.apiUrlBase + environment.getTokenUrl;
  jwtHelper: JwtHelperService = new JwtHelperService();

  constructor( private http : HttpClient ) { 

  }

    //Observable waits for the return of the http to act when it has the response of the server
    save(user: User) : Observable<User>{
      return this.http.post<any>(this.apiURL , user);
    }

    validateToken(user: User) : Observable<any>{
      return this.http.post<any>(this.tokenURL , user);
    }

    getToken(){
      const tokenStr = localStorage.getItem('access_token');
      return JSON.parse(tokenStr).token;
    }

    isAuthenticated() : boolean{

      const token = this.getToken();

      if( token ){
        const expired = this.jwtHelper.isTokenExpired(token);
        return !expired;
      }

      return false;
    }

    getAuthenticatedUser(){
      const token = this.getToken();
      if(token){
        const username = this.jwtHelper.decodeToken(token).username;
        return username;
      }

      return null;
    }

    logout(){
      localStorage.removeItem('access_token');
    }


}
