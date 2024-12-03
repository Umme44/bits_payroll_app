import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPfNomineeEmployeeDetailsDTO } from '../../../shared/model/pf-nominee-employee-details.model';
import { INomineeMaster } from '../../../shared/model/nominee-master.model';
import { INomineeEligibility } from 'app/shared/model/nominee-eligibility';
import { INominee } from '../nominee.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';
import { IEmployeeNomineeInfo } from '../employee-nominee-info.model';

type EntityResponseType = HttpResponse<INominee>;
type EntityResponseTypeForEmployee = HttpResponse<IPfNomineeEmployeeDetailsDTO>;
type EntityArrayResponseType = HttpResponse<INominee[]>;
type EmployeeNomineeInfoArrayResponseType = HttpResponse<IEmployeeNomineeInfo[]>;

@Injectable({ providedIn: 'root' })
export class NomineeService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/nominees';
  public resourceUrlCommon = SERVER_API_URL + 'api/common/nominees';

  constructor(protected http: HttpClient) {}

  create(nominee: INominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .post<INominee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nominee: INominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .put<INominee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INominee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findNomineeCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INominee>(`${this.resourceUrlCommon}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getEmployeeDetailsForNomineeCommon(): Observable<EntityResponseTypeForEmployee> {
    return this.http
      .get<IPfNomineeEmployeeDetailsDTO>(this.resourceUrlCommon + '/employee-details-for-nominee', { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getNomineeList(nominee: INominee): Observable<EntityArrayResponseType> {
    return this.http
      .post<HttpResponse<INominee[]>>(this.resourceUrl + '/list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INominee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  createCommon(file: File, nominee: INominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(nominee);
    formData.append('nominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<INominee>(this.resourceUrlCommon + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateCommon(nominee: INominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .put<INominee>(this.resourceUrlCommon, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateCommonWithFile(file: File, nominee: INominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(nominee);
    formData.append('nominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<INominee>(this.resourceUrlCommon + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INominee>(`${this.resourceUrlCommon}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryCommon(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INominee[]>(this.resourceUrlCommon, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getNomineeListCommon(nominee: INominee): Observable<EntityArrayResponseType> {
    const copy: INominee = this.convertDateFromClient(nominee);
    return this.http
      .post<INominee[]>(this.resourceUrlCommon + '/list', copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  deleteCommon(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlCommon}/${id}`, { observe: 'response' });
  }

  checkEligibility(): Observable<HttpResponse<INomineeEligibility>> {
    return this.http.get<INomineeEligibility>(this.resourceUrlCommon + '/eligibility-check', { observe: 'response' });
  }

  protected convertDateFromClient(nominee: INominee): INominee {
    const copy: INominee = Object.assign({}, nominee, {
      dateOfBirth: nominee.dateOfBirth && nominee.dateOfBirth.isValid() ? nominee.dateOfBirth.format(DATE_FORMAT) : undefined,
      guardianDateOfBirth:
        nominee.guardianDateOfBirth && nominee.guardianDateOfBirth.isValid() ? nominee.guardianDateOfBirth.format(DATE_FORMAT) : undefined,
      nominationDate: nominee.nominationDate && nominee.nominationDate.isValid() ? nominee.nominationDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
      res.body.guardianDateOfBirth = res.body.guardianDateOfBirth ? dayjs(res.body.guardianDateOfBirth) : undefined;
      res.body.nominationDate = res.body.nominationDate ? dayjs(res.body.nominationDate) : undefined;
    }
    return res;
  }

  protected convertDateFromServerForINomineeMaster(res: INomineeMaster): INomineeMaster {
    if (res) {
      res.dateOfBirth = res.dateOfBirth ? dayjs(res.dateOfBirth) : undefined;
      res.dateOfJoining = res.dateOfJoining ? dayjs(res.dateOfJoining) : undefined;
      res.dateOfConfirmation = res.dateOfConfirmation ? dayjs(res.dateOfConfirmation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServerV2(res: EmployeeNomineeInfoArrayResponseType): EmployeeNomineeInfoArrayResponseType {
    if (res.body) {
      res.body.forEach((employee: IEmployeeNomineeInfo) => {
        employee.dateOfBirth = employee.dateOfBirth ? dayjs(employee.dateOfBirth) : undefined;
      });
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((nominee: INominee) => {
        nominee.dateOfBirth = nominee.dateOfBirth ? dayjs(nominee.dateOfBirth) : undefined;
        nominee.guardianDateOfBirth = nominee.guardianDateOfBirth ? dayjs(nominee.guardianDateOfBirth) : undefined;
        nominee.nominationDate = nominee.nominationDate ? dayjs(nominee.nominationDate) : undefined;
      });
    }
    return res;
  }

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }

  getRemainingPercentage(nominee: INominee): Observable<number> {
    return this.http.post<number>(this.resourceUrl + '/get-remaining-percentage', nominee);
  }

  getRemainingPercentageCommon(nominee: INominee): Observable<number> {
    return this.http.post<number>(this.resourceUrlCommon + '/get-remaining-percentage', nominee);
  }

  calculateAge(birthDate: dayjs.Dayjs): number {
    const todayDate = dayjs();
    let years = todayDate.year() - birthDate.year();

    if (todayDate.month() < birthDate.month() || (todayDate.month() === birthDate.month() && todayDate.date() < birthDate.date())) {
      years--;
    }
    return years;
  }

  getEmployeesWithNominees(req?: any): Observable<EmployeeNomineeInfoArrayResponseType> {
    //remove null & undefined values
    Object.keys(req).forEach(k => req[k] === null && delete req[k]);
    Object.keys(req).forEach(k => req[k] === undefined && delete req[k]);
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeNomineeInfo[]>(this.resourceUrl + '/employees-with-nominees', { params: options, observe: 'response' })
      .pipe(map((res: EmployeeNomineeInfoArrayResponseType) => this.convertDateArrayFromServerV2(res)));
  }

  getNomineesByEmployeePin(pin: string): Observable<INomineeMaster> {
    return this.http
      .get<INomineeMaster>(this.resourceUrl + '/all/' + pin)
      .pipe(map((res: INomineeMaster) => this.convertDateFromServerForINomineeMaster(res)));
  }

  getGfNomineesByEmployeeId(employeeId: number): Observable<INominee[]> {
    return this.http.get<INominee[]>(this.resourceUrl + '/all-gf-nominees/' + employeeId);
  }

  getGeneralNomineesByEmployeeId(employeeId: number): Observable<INominee[]> {
    return this.http.get<INominee[]>(this.resourceUrl + '/all-general-nominees/' + employeeId);
  }

  getEmployeeDetailsForNomineeByEmployeeId(employeeId: number): Observable<EntityResponseTypeForEmployee> {
    return this.http
      .get<IPfNomineeEmployeeDetailsDTO>(this.resourceUrl + '/employee-details-for-nominee/' + employeeId, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getNomineeListAdmin(nominee: INominee): Observable<EntityArrayResponseType> {
    const copy: INominee = this.convertDateFromClient(nominee);
    return this.http
      .post<INominee[]>(this.resourceUrl + '/list', copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  createHR(file: File, nominee: INominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(nominee);
    formData.append('nominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<INominee>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateHR(nominee: INominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .put<INominee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithFileHR(file: File, nominee: INominee): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(nominee);
    formData.append('nominee', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<INominee>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public nomineeExportDownload(): Observable<Blob> {
    return this.http.get('/api/payroll-mgt/export/nominee', { responseType: 'blob' });
  }
}
