import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReferences } from '../references.model';

export type PartialUpdateReferences = Partial<IReferences> & Pick<IReferences, 'id'>;

export type EntityResponseType = HttpResponse<IReferences>;
export type EntityArrayResponseType = HttpResponse<IReferences[]>;

@Injectable({ providedIn: 'root' })
export class ReferencesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/references');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(references: IReferences): Observable<EntityResponseType> {
    return this.http.post<IReferences>(this.resourceUrl, references, { observe: 'response' });
  }

  update(references: IReferences): Observable<EntityResponseType> {
    return this.http.put<IReferences>(this.resourceUrl, references, {
      observe: 'response',
    });
  }

  partialUpdate(references: PartialUpdateReferences): Observable<EntityResponseType> {
    return this.http.patch<IReferences>(`${this.resourceUrl}/${this.getReferencesIdentifier(references)}`, references, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReferences>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferences[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByEmployeeId(employeeId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferences[]>(this.resourceUrl + '/get-by-employee/' + employeeId, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReferencesIdentifier(references: Pick<IReferences, 'id'>): number {
    return references.id;
  }

  compareReferences(o1: Pick<IReferences, 'id'> | null, o2: Pick<IReferences, 'id'> | null): boolean {
    return o1 && o2 ? this.getReferencesIdentifier(o1) === this.getReferencesIdentifier(o2) : o1 === o2;
  }

  addReferencesToCollectionIfMissing<Type extends Pick<IReferences, 'id'>>(
    referencesCollection: Type[],
    ...referencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const references: Type[] = referencesToCheck.filter(isPresent);
    if (references.length > 0) {
      const referencesCollectionIdentifiers = referencesCollection.map(referencesItem => this.getReferencesIdentifier(referencesItem)!);
      const referencesToAdd = references.filter(referencesItem => {
        const referencesIdentifier = this.getReferencesIdentifier(referencesItem);
        if (referencesCollectionIdentifiers.includes(referencesIdentifier)) {
          return false;
        }
        referencesCollectionIdentifiers.push(referencesIdentifier);
        return true;
      });
      return [...referencesToAdd, ...referencesCollection];
    }
    return referencesCollection;
  }
}
