import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IOrganization } from '../legacy-model/organization.model';
import { IFileDetails } from '../legacy-model/file-details.model';
import { createRequestOption } from '../../../core/request/request-util';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IOrganization>;
type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationCustomService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/configs/organizations');
  private resourceUrlForBasic = this.applicationConfigService.getEndpointFor('api/common/organizations/basic');
  private resourceUrlForFileLoad = this.applicationConfigService.getEndpointFor('files/organizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.post<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  update(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.put<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  createAlongFiles(organization: IOrganization, files: File[]): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }
    formData.append('organizationDTO', new Blob([JSON.stringify(organization)], { type: 'application/json' }));
    return this.http.post<IOrganization>(this.resourceUrl + '/multipart', formData, { observe: 'response' });
  }

  updateAlongFiles(organization: IOrganization, files: File[]): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }
    formData.append('organizationDTO', new Blob([JSON.stringify(organization)], { type: 'application/json' }));
    return this.http.put<IOrganization>(this.resourceUrl + '/multipart', formData, { observe: 'response' });
  }

  loadLogo(): Observable<any> {
    return this.http.get<IOrganization>(this.resourceUrl + '/logo');
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBasicDetails(): Observable<HttpResponse<IOrganization>> {
    return this.http.get<IOrganization>(this.resourceUrlForBasic, { observe: 'response' });
  }

  getOrganizationFileDetails(): Observable<HttpResponse<IFileDetails>> {
    return this.http.get<IFileDetails>(this.resourceUrlForFileLoad + '/stamp-preview', { observe: 'response' });
  }
}
