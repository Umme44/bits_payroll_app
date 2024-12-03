import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfArrear, NewPfArrear } from '../pf-arrear.model';
import { IQuickFilterDto } from '../../../shared/model/quick-filter-dto';

export type PartialUpdatePfArrear = Partial<IPfArrear> & Pick<IPfArrear, 'id'>;

export type EntityResponseType = HttpResponse<IPfArrear>;
export type EntityArrayResponseType = HttpResponse<IPfArrear[]>;

@Injectable({ providedIn: 'root' })
export class PfArrearService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/pf-arrears');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfArrear: NewPfArrear): Observable<EntityResponseType> {
    return this.http.post<IPfArrear>(`${this.resourceUrl}`, pfArrear, { observe: 'response' });
  }

  update(pfArrear: IPfArrear): Observable<EntityResponseType> {
    return this.http.put<IPfArrear>(`${this.resourceUrl}`, pfArrear, { observe: 'response' });
  }

  partialUpdate(pfArrear: PartialUpdatePfArrear): Observable<EntityResponseType> {
    return this.http.patch<IPfArrear>(`${this.resourceUrl}/${this.getPfArrearIdentifier(pfArrear)}`, pfArrear, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPfArrear>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPfArrear[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfArrearIdentifier(pfArrear: Pick<IPfArrear, 'id'>): number {
    return pfArrear.id;
  }

  comparePfArrear(o1: Pick<IPfArrear, 'id'> | null, o2: Pick<IPfArrear, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfArrearIdentifier(o1) === this.getPfArrearIdentifier(o2) : o1 === o2;
  }

  addPfArrearToCollectionIfMissing<Type extends Pick<IPfArrear, 'id'>>(
    pfArrearCollection: Type[],
    ...pfArrearsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfArrears: Type[] = pfArrearsToCheck.filter(isPresent);
    if (pfArrears.length > 0) {
      const pfArrearCollectionIdentifiers = pfArrearCollection.map(pfArrearItem => this.getPfArrearIdentifier(pfArrearItem)!);
      const pfArrearsToAdd = pfArrears.filter(pfArrearItem => {
        const pfArrearIdentifier = this.getPfArrearIdentifier(pfArrearItem);
        if (pfArrearCollectionIdentifiers.includes(pfArrearIdentifier)) {
          return false;
        }
        pfArrearCollectionIdentifiers.push(pfArrearIdentifier);
        return true;
      });
      return [...pfArrearsToAdd, ...pfArrearCollection];
    }
    return pfArrearCollection;
  }

  queryByFiltering(quickFilterDTO: IQuickFilterDto, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<IPfArrear[]>(this.resourceUrl + '/search', quickFilterDTO, { params: options, observe: 'response' });
  }
}
