import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfLoanRepayment, NewPfLoanRepayment } from '../pf-loan-repayment.model';

export type PartialUpdatePfLoanRepayment = Partial<IPfLoanRepayment> & Pick<IPfLoanRepayment, 'id'>;

type RestOf<T extends IPfLoanRepayment | NewPfLoanRepayment> = Omit<T, 'deductionDate'> & {
  deductionDate?: string | null;
};

export type RestPfLoanRepayment = RestOf<IPfLoanRepayment>;

export type NewRestPfLoanRepayment = RestOf<NewPfLoanRepayment>;

export type PartialUpdateRestPfLoanRepayment = RestOf<PartialUpdatePfLoanRepayment>;

export type EntityResponseType = HttpResponse<IPfLoanRepayment>;
export type EntityArrayResponseType = HttpResponse<IPfLoanRepayment[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanRepaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/pf-loan-repayments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfLoanRepayment: NewPfLoanRepayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanRepayment);
    return this.http
      .post<RestPfLoanRepayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfLoanRepayment: IPfLoanRepayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanRepayment);
    return this.http
      .put<RestPfLoanRepayment>(`${this.resourceUrl}/${this.getPfLoanRepaymentIdentifier(pfLoanRepayment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfLoanRepayment: PartialUpdatePfLoanRepayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanRepayment);
    return this.http
      .patch<RestPfLoanRepayment>(`${this.resourceUrl}/${this.getPfLoanRepaymentIdentifier(pfLoanRepayment)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfLoanRepayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfLoanRepayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfLoanRepayment[]>(`${this.resourceUrl}/${year}/${month}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfLoanRepaymentIdentifier(pfLoanRepayment: Pick<IPfLoanRepayment, 'id'>): number {
    return pfLoanRepayment.id;
  }

  comparePfLoanRepayment(o1: Pick<IPfLoanRepayment, 'id'> | null, o2: Pick<IPfLoanRepayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfLoanRepaymentIdentifier(o1) === this.getPfLoanRepaymentIdentifier(o2) : o1 === o2;
  }

  addPfLoanRepaymentToCollectionIfMissing<Type extends Pick<IPfLoanRepayment, 'id'>>(
    pfLoanRepaymentCollection: Type[],
    ...pfLoanRepaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfLoanRepayments: Type[] = pfLoanRepaymentsToCheck.filter(isPresent);
    if (pfLoanRepayments.length > 0) {
      const pfLoanRepaymentCollectionIdentifiers = pfLoanRepaymentCollection.map(
        pfLoanRepaymentItem => this.getPfLoanRepaymentIdentifier(pfLoanRepaymentItem)!
      );
      const pfLoanRepaymentsToAdd = pfLoanRepayments.filter(pfLoanRepaymentItem => {
        const pfLoanRepaymentIdentifier = this.getPfLoanRepaymentIdentifier(pfLoanRepaymentItem);
        if (pfLoanRepaymentCollectionIdentifiers.includes(pfLoanRepaymentIdentifier)) {
          return false;
        }
        pfLoanRepaymentCollectionIdentifiers.push(pfLoanRepaymentIdentifier);
        return true;
      });
      return [...pfLoanRepaymentsToAdd, ...pfLoanRepaymentCollection];
    }
    return pfLoanRepaymentCollection;
  }

  protected convertDateFromClient<T extends IPfLoanRepayment | NewPfLoanRepayment | PartialUpdatePfLoanRepayment>(
    pfLoanRepayment: T
  ): RestOf<T> {
    return {
      ...pfLoanRepayment,
      deductionDate: pfLoanRepayment.deductionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfLoanRepayment: RestPfLoanRepayment): IPfLoanRepayment {
    return {
      ...restPfLoanRepayment,
      deductionDate: restPfLoanRepayment.deductionDate ? dayjs(restPfLoanRepayment.deductionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPfLoanRepayment>): HttpResponse<IPfLoanRepayment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPfLoanRepayment[]>): HttpResponse<IPfLoanRepayment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfLoanRepayment: IPfLoanRepayment) => {
        pfLoanRepayment.deductionDate = pfLoanRepayment.deductionDate ? dayjs(pfLoanRepayment.deductionDate) : undefined;
      });
    }
    return res;
  }
}
