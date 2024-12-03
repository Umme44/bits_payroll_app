import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Pagination } from 'app/core/request/request.model';
import { IUser } from '../user-management.model';
import { UserFilterDTO } from '../userFilterDTO.model';
import { UserDTOModel } from '../user-dto.model'



@Injectable({ providedIn: 'root' })
export class UserManagementService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/admin/users');
  private userName: any;

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(user: IUser): Observable<IUser> {
    return this.http.post<IUser>(this.resourceUrl, user);
  }

  update(user: IUser): Observable<IUser> {
    return this.http.put<IUser>(this.resourceUrl, user);
  }

  find(login: string): Observable<IUser> {
    return this.http.get<IUser>(`${this.resourceUrl}/${login}`);
  }

  query(req?: Pagination): Observable<HttpResponse<IUser[]>> {
    const options = createRequestOption(req);
    return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  querySearch(userFilter: UserFilterDTO, req?: Pagination): Observable<HttpResponse<IUser[]>> {
    const options = createRequestOption(req);
    return this.http.post<IUser[]>(`${this.resourceUrl}/userSearch`, userFilter, {
      params: options,
      observe: 'response',
    });
  }

  delete(login: string): Observable<{}> {
    return this.http.delete(`${this.resourceUrl}/${login}`);
  }

  authorities(): Observable<string[]> {
    return this.http.get<string[]>(this.applicationConfigService.getEndpointFor('api/admin/users/authorities'));
  }

  changePassword(login: string, newPassword: string): Observable<{}> {
    return this.http.post(this.applicationConfigService.getEndpointFor(`api/admin/users/${login}/change-password`), { newPassword });
  }


  // resetFailedAttempts(username: string): Observable<any> {
  //   return this.http.post<any>(`${this.resourceUrl}/${username}/resetFailedAttempts`, username);
  // }
  //
  // findContinuousFailedAttemptsZero(username: string): Observable<any> {
  //   return this.http.get<any>(`${this.resourceUrl}/${username}/findContinuousFailedAttemptsZero`);
  // }

  // NEW API //



  findFailedLoginAttemptByUsername(userName: string):// @ts-ignore
  // @ts-ignore
  Observable<any>{
    return this.http.get<any>(`${this.resourceUrl}/${userName}/findFailedLoginAttemptByUsername`)

  }

  resetFailedLoginAttempts(userName :string):// @ts-ignore
  Observable<{}> {
    // @ts-ignore
  return this.http.post<any>(`${this.resourceUrl}/${userName}/findFailedLoginAttemptWithMultipleFailures`)

  }

  // findByFour(userName: string):// @ts-ignore
  // // @ts-ignore
  //   Observable<any>{
  //   return this.http.get<any>(`${this.resourceUrl}/${userName}/getUserByFour`)
  //
  // }

  getFailedLoginAttempts(username: string): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/${username}/failed-login-attempts`);
  }

  ////////////NEW API ///////////////////
  // * Fetch failed login attempts for a specific user.

  // getFailedLoginAttemptsOne(username: string): Observable<UserNewDTO> {
  //   const url = `${this.resourceUrl}/${username}`;
  //   return this.http.get<UserNewDTO>(url);
  // }
  //
  // * Reset failed login attempts for a specific user.
  // resetFailedLoginAttemptstwo(username: string): Observable<UserNewDTO> {
  //   const url = `${this.resourceUrl}/${username}/reset`;
  //   return this.http.post<UserNewDTO>(url, null); // Sending POST request with no body
  // }

  resetFailedLoginAttemptsFour(username: string): Observable<UserDTOModel> {
    const url = `${this.resourceUrl}/${username}/reset`;
    return this.http.post<UserDTOModel>(url, null);  // POST request with no body
  }

  /////// reset 0 when exceed 4//////////////////
  lockedLoginAttempts(username: string): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}/users/${username}/resetFailedLoginAttempts`, null);
  }
  /////// count 0 /////////////

  countZero(username :string):Observable<any>{
    return this.http.get<any>(`${this.resourceUrl}/users/${username}/setLoginAttempts`)

  }









}
