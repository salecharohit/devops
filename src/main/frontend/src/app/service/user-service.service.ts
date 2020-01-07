import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs/Observable';
import { Location } from '@angular/common';
 
@Injectable()
export class UserService {
 
  private usersUrl: string;
 
  constructor(private http: HttpClient,private location: Location) {
    this.usersUrl = 'http://'+location.host()+'/users';
  }
 
  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }
 
  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }
}