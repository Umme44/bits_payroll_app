import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEducationDetails } from '../education-details.model';
import { IReferences } from '../../references/references.model';

export type PartialUpdateEducationDetails = Partial<IEducationDetails> & Pick<IEducationDetails, 'id'>;

export type EntityResponseType = HttpResponse<IEducationDetails>;
export type EntityArrayResponseType = HttpResponse<IEducationDetails[]>;

@Injectable({ providedIn: 'root' })
export class EducationDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/education-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(educationDetails: IEducationDetails): Observable<EntityResponseType> {
    return this.http.post<IEducationDetails>(this.resourceUrl, educationDetails, { observe: 'response' });
  }

  update(educationDetails: IEducationDetails): Observable<EntityResponseType> {
    return this.http.put<IEducationDetails>(this.resourceUrl, educationDetails, { observe: 'response' });
  }

  partialUpdate(educationDetails: PartialUpdateEducationDetails): Observable<EntityResponseType> {
    return this.http.patch<IEducationDetails>(
      `${this.resourceUrl}/${this.getEducationDetailsIdentifier(educationDetails)}`,
      educationDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEducationDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEducationDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEducationDetailsIdentifier(educationDetails: Pick<IEducationDetails, 'id'>): number {
    return educationDetails.id;
  }

  compareEducationDetails(o1: Pick<IEducationDetails, 'id'> | null, o2: Pick<IEducationDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getEducationDetailsIdentifier(o1) === this.getEducationDetailsIdentifier(o2) : o1 === o2;
  }

  addEducationDetailsToCollectionIfMissing<Type extends Pick<IEducationDetails, 'id'>>(
    educationDetailsCollection: Type[],
    ...educationDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const educationDetails: Type[] = educationDetailsToCheck.filter(isPresent);
    if (educationDetails.length > 0) {
      const educationDetailsCollectionIdentifiers = educationDetailsCollection.map(
        educationDetailsItem => this.getEducationDetailsIdentifier(educationDetailsItem)!
      );
      const educationDetailsToAdd = educationDetails.filter(educationDetailsItem => {
        const educationDetailsIdentifier = this.getEducationDetailsIdentifier(educationDetailsItem);
        if (educationDetailsCollectionIdentifiers.includes(educationDetailsIdentifier)) {
          return false;
        }
        educationDetailsCollectionIdentifiers.push(educationDetailsIdentifier);
        return true;
      });
      return [...educationDetailsToAdd, ...educationDetailsCollection];
    }
    return educationDetailsCollection;
  }

  queryByEmployeeId(employeeId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferences[]>(this.resourceUrl + '/get-by-employee/' + employeeId, { params: options, observe: 'response' });
  }
}
