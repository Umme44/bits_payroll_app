import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeductionType, NewDeductionType } from '../deduction-type.model';

export type PartialUpdateDeductionType = Partial<IDeductionType> & Pick<IDeductionType, 'id'>;

export type EntityResponseType = HttpResponse<IDeductionType>;
export type EntityArrayResponseType = HttpResponse<IDeductionType[]>;

@Injectable({ providedIn: 'root' })
export class DeductionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/deduction-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deductionType: NewDeductionType): Observable<EntityResponseType> {
    return this.http.post<IDeductionType>(this.resourceUrl, deductionType, { observe: 'response' });
  }

  update(deductionType: IDeductionType): Observable<EntityResponseType> {
    return this.http.put<IDeductionType>(`${this.resourceUrl}/${this.getDeductionTypeIdentifier(deductionType)}`, deductionType, {
      observe: 'response',
    });
  }

  partialUpdate(deductionType: PartialUpdateDeductionType): Observable<EntityResponseType> {
    return this.http.patch<IDeductionType>(`${this.resourceUrl}/${this.getDeductionTypeIdentifier(deductionType)}`, deductionType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeductionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeductionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeductionTypeIdentifier(deductionType: Pick<IDeductionType, 'id'>): number {
    return deductionType.id;
  }

  compareDeductionType(o1: Pick<IDeductionType, 'id'> | null, o2: Pick<IDeductionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeductionTypeIdentifier(o1) === this.getDeductionTypeIdentifier(o2) : o1 === o2;
  }

  addDeductionTypeToCollectionIfMissing<Type extends Pick<IDeductionType, 'id'>>(
    deductionTypeCollection: Type[],
    ...deductionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deductionTypes: Type[] = deductionTypesToCheck.filter(isPresent);
    if (deductionTypes.length > 0) {
      const deductionTypeCollectionIdentifiers = deductionTypeCollection.map(
        deductionTypeItem => this.getDeductionTypeIdentifier(deductionTypeItem)!
      );
      const deductionTypesToAdd = deductionTypes.filter(deductionTypeItem => {
        const deductionTypeIdentifier = this.getDeductionTypeIdentifier(deductionTypeItem);
        if (deductionTypeCollectionIdentifiers.includes(deductionTypeIdentifier)) {
          return false;
        }
        deductionTypeCollectionIdentifiers.push(deductionTypeIdentifier);
        return true;
      });
      return [...deductionTypesToAdd, ...deductionTypeCollection];
    }
    return deductionTypeCollection;
  }
}
