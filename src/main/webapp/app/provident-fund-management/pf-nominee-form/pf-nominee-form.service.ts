import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPfNomineeEmployeeDetailsDTO } from '../../shared/model/pf-nominee-employee-details.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IPfNominee } from '../../shared/legacy/legacy-model/pf-nominee.model';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import dayjs from 'dayjs/esm';

type EntityResponseType = HttpResponse<IPfNominee>;
type EntityArrayResponseType = HttpResponse<IPfNominee[]>;

@Injectable({ providedIn: 'root' })
export class PfNomineeFormService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/pf-nominees-form');
  public mgtResourceUrl = this.applicationConfigService.getEndpointFor('/api/pf-mgt/pf-nominees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

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
      .put<IPfNominee>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithOutFile(pfNominee: IPfNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfNominee);
    return this.http
      .put<IPfNominee>(this.resourceUrl, copy, { observe: 'response' })
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

  queryForList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfNominee[]>(this.resourceUrl + '/list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCurrentEmployeeDetails(): Observable<HttpResponse<IPfNomineeEmployeeDetailsDTO>> {
    return this.http.get<IPfNomineeEmployeeDetailsDTO>(`${this.resourceUrl}/current-employee-details`, { observe: 'response' });
  }

  getTotalConsumedSharePercentageOfCurrentUser(): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/consumed-share-percentage`);
  }

  getRemainingSharePercentageOfCurrentUser(pfNominee: IPfNominee): Observable<number> {
    return this.http.post<number>(`${this.resourceUrl}/remaining-share-percentage`, pfNominee);
  }

  protected convertDateFromClient(pfNominee: IPfNominee): IPfNominee {
    const copy: IPfNominee = Object.assign({}, pfNominee, {
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
        pfNominee.dateOfBirth = pfNominee.dateOfBirth ? dayjs(pfNominee.dateOfBirth) : undefined;
        pfNominee.guardianDateOfBirth = pfNominee.guardianDateOfBirth ? dayjs(pfNominee.guardianDateOfBirth) : undefined;
      });
    }
    return res;
  }

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }
}
