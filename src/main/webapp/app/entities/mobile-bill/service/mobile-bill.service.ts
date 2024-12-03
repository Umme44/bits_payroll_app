import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMobileBill, NewMobileBill } from '../mobile-bill.model';

export type PartialUpdateMobileBill = Partial<IMobileBill> & Pick<IMobileBill, 'id'>;

export type EntityResponseType = HttpResponse<IMobileBill>;
export type EntityArrayResponseType = HttpResponse<IMobileBill[]>;

@Injectable({ providedIn: 'root' })
export class MobileBillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/mobile-bills');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mobileBill: NewMobileBill): Observable<EntityResponseType> {
    return this.http.post<IMobileBill>(this.resourceUrl, mobileBill, { observe: 'response' });
  }

  update(mobileBill: IMobileBill): Observable<EntityResponseType> {
    return this.http.put<IMobileBill>(`${this.resourceUrl}/${this.getMobileBillIdentifier(mobileBill)}`, mobileBill, {
      observe: 'response',
    });
  }

  partialUpdate(mobileBill: PartialUpdateMobileBill): Observable<EntityResponseType> {
    return this.http.patch<IMobileBill>(`${this.resourceUrl}/${this.getMobileBillIdentifier(mobileBill)}`, mobileBill, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMobileBill>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMobileBill[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMobileBill[]>(`${this.resourceUrl}/${year}/${month}`, { params: options, observe: 'response' });
  }

  getMobileBillIdentifier(mobileBill: Pick<IMobileBill, 'id'>): number {
    return mobileBill.id;
  }

  compareMobileBill(o1: Pick<IMobileBill, 'id'> | null, o2: Pick<IMobileBill, 'id'> | null): boolean {
    return o1 && o2 ? this.getMobileBillIdentifier(o1) === this.getMobileBillIdentifier(o2) : o1 === o2;
  }

  addMobileBillToCollectionIfMissing<Type extends Pick<IMobileBill, 'id'>>(
    mobileBillCollection: Type[],
    ...mobileBillsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mobileBills: Type[] = mobileBillsToCheck.filter(isPresent);
    if (mobileBills.length > 0) {
      const mobileBillCollectionIdentifiers = mobileBillCollection.map(mobileBillItem => this.getMobileBillIdentifier(mobileBillItem)!);
      const mobileBillsToAdd = mobileBills.filter(mobileBillItem => {
        const mobileBillIdentifier = this.getMobileBillIdentifier(mobileBillItem);
        if (mobileBillCollectionIdentifiers.includes(mobileBillIdentifier)) {
          return false;
        }
        mobileBillCollectionIdentifiers.push(mobileBillIdentifier);
        return true;
      });
      return [...mobileBillsToAdd, ...mobileBillCollection];
    }
    return mobileBillCollection;
  }
}
