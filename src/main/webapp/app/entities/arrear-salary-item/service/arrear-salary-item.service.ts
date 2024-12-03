import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArrearSalaryItem, NewArrearSalaryItem } from '../arrear-salary-item.model';

export type PartialUpdateArrearSalaryItem = Partial<IArrearSalaryItem> & Pick<IArrearSalaryItem, 'id'>;

export type EntityResponseType = HttpResponse<IArrearSalaryItem>;
export type EntityArrayResponseType = HttpResponse<IArrearSalaryItem[]>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/arrear-salary-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(arrearSalaryItem: NewArrearSalaryItem): Observable<EntityResponseType> {
    return this.http.post<IArrearSalaryItem>(this.resourceUrl, arrearSalaryItem, { observe: 'response' });
  }

  update(arrearSalaryItem: IArrearSalaryItem): Observable<EntityResponseType> {
    return this.http.put<IArrearSalaryItem>(
      `${this.resourceUrl}/${this.getArrearSalaryItemIdentifier(arrearSalaryItem)}`,
      arrearSalaryItem,
      { observe: 'response' }
    );
  }

  partialUpdate(arrearSalaryItem: PartialUpdateArrearSalaryItem): Observable<EntityResponseType> {
    return this.http.patch<IArrearSalaryItem>(
      `${this.resourceUrl}/${this.getArrearSalaryItemIdentifier(arrearSalaryItem)}`,
      arrearSalaryItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArrearSalaryItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArrearSalaryItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArrearSalaryItemIdentifier(arrearSalaryItem: Pick<IArrearSalaryItem, 'id'>): number {
    return arrearSalaryItem.id;
  }

  compareArrearSalaryItem(o1: Pick<IArrearSalaryItem, 'id'> | null, o2: Pick<IArrearSalaryItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getArrearSalaryItemIdentifier(o1) === this.getArrearSalaryItemIdentifier(o2) : o1 === o2;
  }

  addArrearSalaryItemToCollectionIfMissing<Type extends Pick<IArrearSalaryItem, 'id'>>(
    arrearSalaryItemCollection: Type[],
    ...arrearSalaryItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const arrearSalaryItems: Type[] = arrearSalaryItemsToCheck.filter(isPresent);
    if (arrearSalaryItems.length > 0) {
      const arrearSalaryItemCollectionIdentifiers = arrearSalaryItemCollection.map(
        arrearSalaryItemItem => this.getArrearSalaryItemIdentifier(arrearSalaryItemItem)!
      );
      const arrearSalaryItemsToAdd = arrearSalaryItems.filter(arrearSalaryItemItem => {
        const arrearSalaryItemIdentifier = this.getArrearSalaryItemIdentifier(arrearSalaryItemItem);
        if (arrearSalaryItemCollectionIdentifiers.includes(arrearSalaryItemIdentifier)) {
          return false;
        }
        arrearSalaryItemCollectionIdentifiers.push(arrearSalaryItemIdentifier);
        return true;
      });
      return [...arrearSalaryItemsToAdd, ...arrearSalaryItemCollection];
    }
    return arrearSalaryItemCollection;
  }
}
