import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEmployeeSpecializedSearch } from './EmployeeSpecializedSearch.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IConfig } from '../../legacy/legacy-model/config.model';
import { IEmployee } from '../../legacy/legacy-model/employee.model';

type EntityResponseType = HttpResponse<IEmployeeSpecializedSearch>;

@Injectable({ providedIn: 'root' })
export class SearchModalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/special-search-detail');
  public getcurrentEmployeeResourceUrl = this.applicationConfigService.getEndpointFor('api/common/employees/current');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCurrentEmployee(): Observable<HttpResponse<IEmployee>> {
    return this.http.get<IEmployee>(this.getcurrentEmployeeResourceUrl, { observe: 'response' });
  }
}
