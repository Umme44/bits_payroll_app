import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganization, NewOrganization } from '../organization.model';
import { IFileDetails } from '../../../shared/legacy/legacy-model/file-details.model';

export type PartialUpdateOrganization = Partial<IOrganization> & Pick<IOrganization, 'id'>;

export type EntityResponseType = HttpResponse<IOrganization>;
export type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configs/organizations');
  protected resourceUrlForBasic = this.applicationConfigService.getEndpointFor('api/common/organizations/basic');
  protected resourceUrlForFileLoad = this.applicationConfigService.getEndpointFor('files/organizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organization: NewOrganization): Observable<EntityResponseType> {
    return this.http.post<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  update(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.put<IOrganization>(`${this.resourceUrl}/${this.getOrganizationIdentifier(organization)}`, organization, {
      observe: 'response',
    });
  }

  partialUpdate(organization: PartialUpdateOrganization): Observable<EntityResponseType> {
    return this.http.patch<IOrganization>(`${this.resourceUrl}/${this.getOrganizationIdentifier(organization)}`, organization, {
      observe: 'response',
    });
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

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getBasicDetails(): Observable<HttpResponse<IOrganization>> {
    return this.http.get<IOrganization>(this.resourceUrlForBasic, { observe: 'response' });
  }

  getOrganizationFileDetails(): Observable<HttpResponse<IFileDetails>> {
    return this.http.get<IFileDetails>(this.resourceUrlForFileLoad + '/stamp-preview', { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationIdentifier(organization: Pick<IOrganization, 'id'>): number {
    return organization.id;
  }

  compareOrganization(o1: Pick<IOrganization, 'id'> | null, o2: Pick<IOrganization, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationIdentifier(o1) === this.getOrganizationIdentifier(o2) : o1 === o2;
  }

  addOrganizationToCollectionIfMissing<Type extends Pick<IOrganization, 'id'>>(
    organizationCollection: Type[],
    ...organizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizations: Type[] = organizationsToCheck.filter(isPresent);
    if (organizations.length > 0) {
      const organizationCollectionIdentifiers = organizationCollection.map(
        organizationItem => this.getOrganizationIdentifier(organizationItem)!
      );
      const organizationsToAdd = organizations.filter(organizationItem => {
        const organizationIdentifier = this.getOrganizationIdentifier(organizationItem);
        if (organizationCollectionIdentifiers.includes(organizationIdentifier)) {
          return false;
        }
        organizationCollectionIdentifiers.push(organizationIdentifier);
        return true;
      });
      return [...organizationsToAdd, ...organizationCollection];
    }
    return organizationCollection;
  }
}
