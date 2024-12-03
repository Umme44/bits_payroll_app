import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDepartment } from '../legacy-model/department.model';
import { createRequestOption } from '../../../core/request/request-util';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IDepartment>;
type EntityArrayResponseType = HttpResponse<IDepartment[]>;

@Injectable({ providedIn: 'root' })
export class DepartmentService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/departments';
  public userResourceUrl = SERVER_API_URL + 'api/common/departments';

  constructor(protected http: HttpClient) {}

  create(department: IDepartment): Observable<EntityResponseType> {
    return this.http.post<IDepartment>(this.resourceUrl, department, { observe: 'response' });
  }

  update(department: IDepartment): Observable<EntityResponseType> {
    return this.http.put<IDepartment>(this.resourceUrl, department, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  commonQuery(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartment[]>(this.userResourceUrl, { params: options, observe: 'response' });
  }
}
