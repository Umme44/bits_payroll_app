import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDesignation } from '../legacy-model/designation.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IDesignation>;
type EntityArrayResponseType = HttpResponse<IDesignation[]>;

@Injectable({ providedIn: 'root' })
export class DesignationService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/designations';
  public userResourceUrl = SERVER_API_URL + 'api/common/designations';

  constructor(protected http: HttpClient) {}

  create(designation: IDesignation): Observable<EntityResponseType> {
    return this.http.post<IDesignation>(this.resourceUrl, designation, { observe: 'response' });
  }

  update(designation: IDesignation): Observable<EntityResponseType> {
    return this.http.put<IDesignation>(this.resourceUrl, designation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDesignation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesignation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  commonQuery(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesignation[]>(this.userResourceUrl, { params: options, observe: 'response' });
  }
}
