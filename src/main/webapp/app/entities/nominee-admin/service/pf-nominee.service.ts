import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IPfNominee } from '../pf-nominee.model';
import { PfAccountService } from '../../pf-account/service/pf-account.service';
import { createRequestOption } from '../../../core/request/request-util';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { IPfAccount } from '../../pf-account/pf-account.model';
import { IPfNomineeEmployeeDetailsDTO } from '../../../shared/model/pf-nominee-employee-details.model';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IPfNominee>;
type EntityArrayResponseType = HttpResponse<IPfNominee[]>;

@Injectable({ providedIn: 'root' })
export class PfNomineeService {
  public resourceUrl = SERVER_API_URL + 'api/pf-mgt/pf-nominees';
  public resourceUrlV = SERVER_API_URL + 'api/common/pf-nominees-form';

  constructor(protected http: HttpClient, protected pfAccountService: PfAccountService) {}

  create(file: File, pfNominee: IPfNominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(pfNominee);
    formData.append('pfNominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<IPfNominee>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(file: File, pfNominee: IPfNominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(pfNominee);
    formData.append('pfNominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<IPfNominee>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithOutFile(pfNominee: IPfNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfNominee);
    return this.http
      .put<IPfNominee>(this.resourceUrl + '/without-file', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPfNominee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfNominee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllByPfAccountId(id: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfNominee[]>(this.resourceUrl + '/get-all-by-pf-account-id/' + id, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfAccountsWithPfNominees(filter: Filter): Observable<HttpResponse<IPfAccount[]>> {
    return this.http
      .post<IPfAccount[]>(this.resourceUrl + '/get-pf-accounts-with-pf-nominee', filter, { observe: 'response' })
      .pipe(map((res: HttpResponse<IPfAccount[]>) => this.pfAccountService.convertDateArrayFromServer(res)));
  }

  getRemainingSharePercentage(pfAccountId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/remaining-share-percentage/${pfAccountId}`);
  }

  getRemainingSharePercentageForSavedPfNominee(pfNominee: IPfNominee): Observable<number> {
    return this.http.post<number>(`${this.resourceUrl}/remaining-share-percentage`, pfNominee);
  }

  getEmployeeDetails(pin: any): Observable<HttpResponse<IPfNomineeEmployeeDetailsDTO>> {
    return this.http.get<IPfNomineeEmployeeDetailsDTO>(`${this.resourceUrl}/employee-details/${pin}`, { observe: 'response' });
  }

  protected convertDateFromClient(pfNominee: IPfNominee): IPfNominee {
    const copy: IPfNominee = Object.assign({}, pfNominee, {
      nominationDate:
        pfNominee.nominationDate && pfNominee.nominationDate.isValid() ? pfNominee.nominationDate.format(DATE_FORMAT) : undefined,
      dateOfBirth: pfNominee.dateOfBirth && pfNominee.dateOfBirth.isValid() ? pfNominee.dateOfBirth.format(DATE_FORMAT) : undefined,
      guardianDateOfBirth:
        pfNominee.guardianDateOfBirth && pfNominee.guardianDateOfBirth.isValid()
          ? pfNominee.guardianDateOfBirth.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.nominationDate = res.body.nominationDate ? dayjs(res.body.nominationDate) : undefined;
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.guardianDateOfBirth = res.body.guardianDateOfBirth ? dayjs(res.body.guardianDateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfNominee: IPfNominee) => {
        pfNominee.nominationDate = pfNominee.nominationDate ? dayjs(pfNominee.nominationDate) : undefined;
        pfNominee.dateOfBirth = pfNominee.dateOfBirth ? dayjs(pfNominee.dateOfBirth) : undefined;
        pfNominee.guardianDateOfBirth = pfNominee.guardianDateOfBirth ? dayjs(pfNominee.guardianDateOfBirth) : undefined;
      });
    }
    return res;
  }

  queryForCurrentEmployeeNomineeList(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IPfNominee[]>(`${this.resourceUrl}/get-all-by-pf-account-id/${id}`, { observe: 'response' });
  }

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }

  queryForPfList(pin: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfNominee[]>(this.resourceUrl + '/list/' + pin, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getCurrentEmployeeDetails(pin: string): Observable<HttpResponse<IPfNomineeEmployeeDetailsDTO>> {
    return this.http.get<IPfNomineeEmployeeDetailsDTO>(`${this.resourceUrl}/employee-details/${pin}`, { observe: 'response' });
  }

  getTotalConsumedSharePercentageOfCurrentUser(pfAccountId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/consumed-share-percentage/${pfAccountId}`);
  }
}
