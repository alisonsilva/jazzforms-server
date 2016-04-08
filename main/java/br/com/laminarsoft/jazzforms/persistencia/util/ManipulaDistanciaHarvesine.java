package br.com.laminarsoft.jazzforms.persistencia.util;

public class ManipulaDistanciaHarvesine {
	public static final double RAIO_TERRA = 6371; //distancia aproximada em kilometros
	
	public static double getSphericalLawOfCosines(double lat1, double lon1, double lat2, double lon2) {
		double distancia = 0;

		lat1 = deg2rad(lat1);
		lat2 = deg2rad(lat2);
		lon1 = deg2rad(lon1);
		lon2 = deg2rad(lon2);
		distancia = Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2 - lon1))*RAIO_TERRA ;
		return distancia;
	}
	
	public static double getHarvesine(double lat1, double lon1, double lat2, double lon2) {
		double distancia = 0;

		double dLat = deg2rad((lat2 - lat1));
		double dLon = deg2rad(lon2 - lon1);
		
		lat1 = deg2rad(lat1);
		lat2 = deg2rad(lat2);
		
		double tmp1 = Math.sin(dLat/2)*Math.sin(dLat/2) + Math.sin(dLon/2)*Math.sin(dLon/2)*Math.cos(lat1)*Math.cos(lat2);
		double tmp2 = 2*Math.atan2(Math.sqrt(tmp1), Math.sqrt(1 - tmp1));
		
		
		distancia = RAIO_TERRA*tmp2;
		return distancia;		
	}
	
	public static boolean verificaPontoPresente(double lat, double lon, double plat, double plon, double distancia) {
		double maxLat = getMaxLatitude(lat, distancia);
		double minLat = getMinLatitude(lat, distancia);
		
		double maxLon = getMaxLongitude(lat, lon, distancia);
		double minLon = getMinLongitude(lat, lon, distancia);
		
		if (!(plat > minLat && plat < maxLat && 
				plon > minLon && plon < maxLon)) {
			return false;
		}
		
		double distReal = getSphericalLawOfCosines(lat, lon, plat, plon);
		if (distReal > distancia) {
			return false;
		}		
		
		return true;
	}

	/**
	 * recupera a longitude mínima para que uma determinada longitude esteja para satisfazer a distânica passada
	 * @param latitude Latitude do ponto para onde se deseja obter uma longitude mínima para uma determinada distância
	 * @param longitude Longitude do ponto para onde se deseja obter uma longitude mínima para uma determinada distância
	 * @param distancia Distância de referência para o ponto (latitude, longitude) passados.
	 * @return A menor longitude necessária para que um ponto possa estar num raio "distancia" do ponto de referência
	 */
	public static double getMinLongitude(double latitude, double longitude,
			double distancia) {
		return longitude - rad2deg(distancia/RAIO_TERRA/Math.cos(deg2rad(latitude)));
	}

	/**
	 * Recupera a longitude máxima para que uma determinada longitude esteja para satisfazer a distância passada
	 * @param latitude Latitude do ponto para onde se deseja obter uma longitude mínima para uma determinada distância
	 * @param longitude Longitude do ponto para onde se deseja obter uma longitude mínima para uma determinada distância
	 * @param distancia Distância de referência para o ponto (latitude, longitude) passados
	 * @return A maior longitude necessária para que um ponto possa estar num raio "distância" do ponto de referência
	 */
	public static double getMaxLongitude(double latitude, double longitude, double distancia) {
		return longitude + rad2deg(distancia/RAIO_TERRA/Math.cos(deg2rad(latitude)));
	}

	/**
	 * Recupera a latitude mínima para que uma determinada latitude esteja para satisfazer a distância passada a partir de um ponto de referência
	 * @param latitude Latitude de referência
	 * @param distancia Distância de referência
	 * @return Latitude mínima que o outro ponto deve estar do ponto de referência para que esteja no máximo à "distancia" passada.
	 */
	public static double getMinLatitude(double latitude, double distancia) {
		return latitude - rad2deg(distancia/RAIO_TERRA);
	}
	
	/**
	 * Recupera a latitude máxima para que uma determinada latitude esteja para satisfazer a distância passada a partir de um ponto de referência
	 * @param latitude Latitude de referência
	 * @param distancia Distância de referência
	 * @return Latitude máxima que o outro ponto deve estar do ponto de referência para que esteja no máximo à "distancia" passada.
	 */
	public static double getMaxLatitude(double latitude, double distancia) {
		return latitude + rad2deg(distancia/RAIO_TERRA);
	}
	
	
	/**
	 * transforma graus em radianos
	 * @param valor Valor em graus a serem convertidos
	 * @return O valor em radianos representando o valor em graus passados
	 */
	public static double deg2rad(double valor) {
		return valor*Math.PI/180;
	}
	
	/**
	 * 
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return rad / Math.PI*180.0;
	}
}
