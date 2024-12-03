import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncomeTaxChallan, NewIncomeTaxChallan } from '../income-tax-challan.model';

export type PartialUpdateIncomeTaxChallan = Partial<IIncomeTaxChallan> & Pick<IIncomeTaxChallan, 'id'>;

type RestOf<T extends IIncomeTaxChallan | NewIncomeTaxChallan> = Omit<T, 'challanDate'> & {
  challanDate?: string | null;
};

export type RestIncomeTaxChallan = RestOf<IIncomeTaxChallan>;

export type NewRestIncomeTaxChallan = RestOf<NewIncomeTaxChallan>;

export type PartialUpdateRestIncomeTaxChallan = RestOf<PartialUpdateIncomeTaxChallan>;

export type EntityResponseType = HttpResponse<IIncomeTaxChallan>;
export type EntityArrayResponseType = HttpResponse<IIncomeTaxChallan[]>;

@Injectable({ providedIn: 'root' })
export class IncomeTaxChallanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/income-tax-challans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incomeTaxChallan: NewIncomeTaxChallan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeTaxChallan);
    return this.http
      .post<RestIncomeTaxChallan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incomeTaxChallan: IIncomeTaxChallan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeTaxChallan);
    return this.http
      .put<RestIncomeTaxChallan>(this.resourceUrl, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incomeTaxChallan: PartialUpdateIncomeTaxChallan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeTaxChallan);
    return this.http
      .patch<RestIncomeTaxChallan>(`${this.resourceUrl}/${this.getIncomeTaxChallanIdentifier(incomeTaxChallan)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIncomeTaxChallan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncomeTaxChallan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIncomeTaxChallanIdentifier(incomeTaxChallan: Pick<IIncomeTaxChallan, 'id'>): number {
    return incomeTaxChallan.id;
  }

  compareIncomeTaxChallan(o1: Pick<IIncomeTaxChallan, 'id'> | null, o2: Pick<IIncomeTaxChallan, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncomeTaxChallanIdentifier(o1) === this.getIncomeTaxChallanIdentifier(o2) : o1 === o2;
  }

  addIncomeTaxChallanToCollectionIfMissing<Type extends Pick<IIncomeTaxChallan, 'id'>>(
    incomeTaxChallanCollection: Type[],
    ...incomeTaxChallansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incomeTaxChallans: Type[] = incomeTaxChallansToCheck.filter(isPresent);
    if (incomeTaxChallans.length > 0) {
      const incomeTaxChallanCollectionIdentifiers = incomeTaxChallanCollection.map(
        incomeTaxChallanItem => this.getIncomeTaxChallanIdentifier(incomeTaxChallanItem)!
      );
      const incomeTaxChallansToAdd = incomeTaxChallans.filter(incomeTaxChallanItem => {
        const incomeTaxChallanIdentifier = this.getIncomeTaxChallanIdentifier(incomeTaxChallanItem);
        if (incomeTaxChallanCollectionIdentifiers.includes(incomeTaxChallanIdentifier)) {
          return false;
        }
        incomeTaxChallanCollectionIdentifiers.push(incomeTaxChallanIdentifier);
        return true;
      });
      return [...incomeTaxChallansToAdd, ...incomeTaxChallanCollection];
    }
    return incomeTaxChallanCollection;
  }

  protected convertDateFromClient<T extends IIncomeTaxChallan | NewIncomeTaxChallan | PartialUpdateIncomeTaxChallan>(
    incomeTaxChallan: T
  ): RestOf<T> {
    return {
      ...incomeTaxChallan,
      challanDate: incomeTaxChallan.challanDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restIncomeTaxChallan: RestIncomeTaxChallan): IIncomeTaxChallan {
    return {
      ...restIncomeTaxChallan,
      challanDate: restIncomeTaxChallan.challanDate ? dayjs(restIncomeTaxChallan.challanDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIncomeTaxChallan>): HttpResponse<IIncomeTaxChallan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIncomeTaxChallan[]>): HttpResponse<IIncomeTaxChallan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  checkChallanNo(challanNo: String): Observable<HttpResponse<Boolean>> {
    return this.http.get<Boolean>(`${this.resourceUrl}/check-duplicate-challanNo/${challanNo}`, { observe: 'response' });
  }
}
