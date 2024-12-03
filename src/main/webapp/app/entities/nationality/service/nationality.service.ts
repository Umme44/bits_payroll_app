import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INationality, NewNationality } from '../nationality.model';

export type PartialUpdateNationality = Partial<INationality> & Pick<INationality, 'id'>;

export type EntityResponseType = HttpResponse<INationality>;
export type EntityArrayResponseType = HttpResponse<INationality[]>;

@Injectable({ providedIn: 'root' })
export class NationalityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/nationalities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nationality: NewNationality): Observable<EntityResponseType> {
    return this.http.post<INationality>(this.resourceUrl, nationality, { observe: 'response' });
  }

  update(nationality: INationality): Observable<EntityResponseType> {
    return this.http.put<INationality>(this.resourceUrl, nationality, {
      observe: 'response',
    });
  }

  partialUpdate(nationality: PartialUpdateNationality): Observable<EntityResponseType> {
    return this.http.patch<INationality>(`${this.resourceUrl}/${this.getNationalityIdentifier(nationality)}`, nationality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INationality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INationality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryPage(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INationality[]>(`${this.resourceUrl}/page`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNationalityIdentifier(nationality: Pick<INationality, 'id'>): number {
    return nationality.id;
  }

  compareNationality(o1: Pick<INationality, 'id'> | null, o2: Pick<INationality, 'id'> | null): boolean {
    return o1 && o2 ? this.getNationalityIdentifier(o1) === this.getNationalityIdentifier(o2) : o1 === o2;
  }

  addNationalityToCollectionIfMissing<Type extends Pick<INationality, 'id'>>(
    nationalityCollection: Type[],
    ...nationalitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const nationalities: Type[] = nationalitiesToCheck.filter(isPresent);
    if (nationalities.length > 0) {
      const nationalityCollectionIdentifiers = nationalityCollection.map(
        nationalityItem => this.getNationalityIdentifier(nationalityItem)!
      );
      const nationalitiesToAdd = nationalities.filter(nationalityItem => {
        const nationalityIdentifier = this.getNationalityIdentifier(nationalityItem);
        if (nationalityCollectionIdentifiers.includes(nationalityIdentifier)) {
          return false;
        }
        nationalityCollectionIdentifiers.push(nationalityIdentifier);
        return true;
      });
      return [...nationalitiesToAdd, ...nationalityCollection];
    }
    return nationalityCollection;
  }
}
