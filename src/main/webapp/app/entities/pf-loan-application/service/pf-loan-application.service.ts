import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfLoanApplication, NewPfLoanApplication } from '../pf-loan-application.model';
import {
  IPfLoanApplicationEligible,
  PfLoanApplicationEligible
} from '../../../shared/model/pf-loan-application-eligible.model';
import { IEmployeeBankDetails } from '../../../shared/model/employee-bank-details.model';

export type PartialUpdatePfLoanApplication = Partial<IPfLoanApplication> & Pick<IPfLoanApplication, 'id'>;

type RestOf<T extends IPfLoanApplication | NewPfLoanApplication> = Omit<
  T,
  'recommendationDate' | 'approvalDate' | 'rejectionDate' | 'disbursementDate'
> & {
  recommendationDate?: string | null;
  approvalDate?: string | null;
  rejectionDate?: string | null;
  disbursementDate?: string | null;
};

export type RestPfLoanApplication = RestOf<IPfLoanApplication>;

export type NewRestPfLoanApplication = RestOf<NewPfLoanApplication>;

export type PartialUpdateRestPfLoanApplication = RestOf<PartialUpdatePfLoanApplication>;
type EmployeeBankDetailsResponseType = HttpResponse<IEmployeeBankDetails>;

export type EntityResponseType = HttpResponse<IPfLoanApplication>;
export type EntityArrayResponseType = HttpResponse<IPfLoanApplication[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationService {
  // protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-loan-applications');
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-loan-applications');

  /* api/pf-mgt/pf-loan-applications */

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfLoanApplication: NewPfLoanApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplication);
    return this.http
      .post<RestPfLoanApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfLoanApplication: IPfLoanApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplication);
    return this.http
      .put<RestPfLoanApplication>(`${this.resourceUrl}/${this.getPfLoanApplicationIdentifier(pfLoanApplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfLoanApplication: PartialUpdatePfLoanApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplication);
    return this.http
      .patch<RestPfLoanApplication>(`${this.resourceUrl}/${this.getPfLoanApplicationIdentifier(pfLoanApplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfLoanApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfLoanApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfLoanApplicationIdentifier(pfLoanApplication: Pick<IPfLoanApplication, 'id'>): number {
    return pfLoanApplication.id;
  }

  comparePfLoanApplication(o1: Pick<IPfLoanApplication, 'id'> | null, o2: Pick<IPfLoanApplication, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfLoanApplicationIdentifier(o1) === this.getPfLoanApplicationIdentifier(o2) : o1 === o2;
  }

  addPfLoanApplicationToCollectionIfMissing<Type extends Pick<IPfLoanApplication, 'id'>>(
    pfLoanApplicationCollection: Type[],
    ...pfLoanApplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfLoanApplications: Type[] = pfLoanApplicationsToCheck.filter(isPresent);
    if (pfLoanApplications.length > 0) {
      const pfLoanApplicationCollectionIdentifiers = pfLoanApplicationCollection.map(
        pfLoanApplicationItem => this.getPfLoanApplicationIdentifier(pfLoanApplicationItem)!
      );
      const pfLoanApplicationsToAdd = pfLoanApplications.filter(pfLoanApplicationItem => {
        const pfLoanApplicationIdentifier = this.getPfLoanApplicationIdentifier(pfLoanApplicationItem);
        if (pfLoanApplicationCollectionIdentifiers.includes(pfLoanApplicationIdentifier)) {
          return false;
        }
        pfLoanApplicationCollectionIdentifiers.push(pfLoanApplicationIdentifier);
        return true;
      });
      return [...pfLoanApplicationsToAdd, ...pfLoanApplicationCollection];
    }
    return pfLoanApplicationCollection;
  }

  protected convertDateFromClient<T extends IPfLoanApplication | NewPfLoanApplication | PartialUpdatePfLoanApplication>(
    pfLoanApplication: T
  ): RestOf<T> {
    return {
      ...pfLoanApplication,
      recommendationDate: pfLoanApplication.recommendationDate?.format(DATE_FORMAT) ?? null,
      approvalDate: pfLoanApplication.approvalDate?.format(DATE_FORMAT) ?? null,
      rejectionDate: pfLoanApplication.rejectionDate?.format(DATE_FORMAT) ?? null,
      disbursementDate: pfLoanApplication.disbursementDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfLoanApplication: RestPfLoanApplication): IPfLoanApplication {
    return {
      ...restPfLoanApplication,
      recommendationDate: restPfLoanApplication.recommendationDate ? dayjs(restPfLoanApplication.recommendationDate) : undefined,
      approvalDate: restPfLoanApplication.approvalDate ? dayjs(restPfLoanApplication.approvalDate) : undefined,
      rejectionDate: restPfLoanApplication.rejectionDate ? dayjs(restPfLoanApplication.rejectionDate) : undefined,
      disbursementDate: restPfLoanApplication.disbursementDate ? dayjs(restPfLoanApplication.disbursementDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPfLoanApplication>): HttpResponse<IPfLoanApplication> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPfLoanApplication[]>): HttpResponse<IPfLoanApplication[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
  getPfLoanApplicationEligibility(pfAccountId: number): Observable<HttpResponse<PfLoanApplicationEligible>> {
    return this.http.get<IPfLoanApplicationEligible>(this.resourceUrl + '/check-eligibility/' + pfAccountId, { observe: 'response' });
  }

  getEmployeeBankDetailsByPin(pin: string): Observable<EmployeeBankDetailsResponseType> {
    return this.http.get<IEmployeeBankDetails>(this.resourceUrl + '/bank-details/employee/' + pin, { observe: 'response' });
  }
}
