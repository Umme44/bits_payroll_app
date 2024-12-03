import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IConfig } from '../legacy-model/config.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { IAllowanceName } from '../../model/allowance-name.model';

type EntityResponseType = HttpResponse<IConfig>;
type EntityArrayResponseType = HttpResponse<IConfig[]>;

@Injectable({ providedIn: 'root' })
export class ConfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/configs');
  public resourceUrlProcurementOfficer = this.applicationConfigService.getEndpointFor('api/procurement-mgt/configs');
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/common/configs/key');
  public resourceUrlAllowanceName = this.applicationConfigService.getEndpointFor('api/employee-mgt/allowance-name');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(config: IConfig): Observable<EntityResponseType> {
    return this.http.post<IConfig>(this.resourceUrl, config, { observe: 'response' });
  }

  update(config: IConfig): Observable<EntityResponseType> {
    return this.http.put<IConfig>(this.resourceUrl, config, { observe: 'response' });
  }

  updateByProcurementOfficer(config: IConfig): Observable<EntityResponseType> {
    return this.http.put<IConfig>(this.resourceUrlProcurementOfficer, config, { observe: 'response' });
  }

  findByKeyByProcurementOfficer(key: string): Observable<EntityResponseType> {
    return this.http.get<IConfig>(`${this.resourceUrlProcurementOfficer}/key/${key}`, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByKey(key: string): Observable<EntityResponseType> {
    return this.http.get<IConfig>(`${this.resourceUrl}/key/${key}`, { observe: 'response' });
  }

  findByKeyCommon(key: string): Observable<EntityResponseType> {
    return this.http.get<IConfig>(`${this.resourceUrlCommon}/${key}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllowanceName(): Observable<HttpResponse<IAllowanceName>> {
    return this.http.get<IAllowanceName>(`${this.resourceUrlAllowanceName}`, { observe: 'response' });
  }
}
